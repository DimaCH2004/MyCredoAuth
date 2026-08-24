# MyCredo — Negative Authorization Tests

UI test suite covering the negative cases of the MyCredo authorization page at
<https://mycredo.ge/landing/main/auth>, executed across every interface language the application
ships.

Selenium 4 · TestNG 7 · WebDriverManager · Allure · Java 17

---

## Running

```bash
mvn clean test
```

```bash
mvn allure:serve
```

Useful overrides:

| Property | Default | Purpose |
|---|---|---|
| `-Dthreads=N` | `2` | Parallel threads. `1` runs serially. |
| `-Dheadless=true` | `false` | See the note on headless below. |
| `-DbaseUrl=...` | authorization page | Point the suite at another environment. |
| `-Dtimeout.fluent=N` | `25` | Wait ceiling, in seconds. |
| `-Dtimeout.poll=N` | `300` | Polling interval, in milliseconds. |

---

## Layout

```
src/main/java/
  Data/       Constants, Language, TestCredentials, BlankField, DataSets
  Page/       locators only — BasePage, LoginPage, LanguagePopup, RegistrationPage
  Steps/      actions and assertions, fluent — BaseSteps + one class per page
  Utils/      DriverManager, BrowserConfig, WaitUtils
  Listeners/  TestListener
src/test/java/org/example/
  LoginNegativeScenario
```

A **Page** declares what is on screen and nothing else. A **Steps** class does the acting and the
asserting, returning `this` so a scenario reads as one chain:

```java
loginSteps().switchLanguageTo(language)
        .signInWith(credentials)
        .shouldShowInvalidCredentialsToast(language)
        .shouldStayOnAuthPage()
        .assertAll();
```

The test class holds no locators, no data and no branching.

---

## Coverage

Six scenarios, each run once per language — 30 tests in total.

| Scenario | Expected |
|---|---|
| Empty form submitted | Both fields report the required-field error; no request is sent |
| One field left empty | That field alone is flagged |
| Invalid credentials — random, whitespace-only, SQL tautology, 300 chars | The same generic localised refusal for all four |
| Registration: empty personal number | Required-field error |
| Registration: personal number under 11 characters | Localised length error |
| Registration: missing birth date | Required-field error |

The four invalid-credential payloads deliberately expect an *identical* message. Answering a
malformed payload differently from a merely wrong one would let an attacker tell them apart.

Nothing in the suite signs in successfully — the scope is negative cases only.

---

## Notes on the application

Things found while writing this that shape how the suite behaves.

**Languages.** The task asks for Georgian, Mingrelian and Svan. The application ships five
languages — `ka`, `eng`, `rus`, `am`, `az` — and offers neither Mingrelian nor Svan; searching
every loaded JS bundle for `მეგრული` and `სვანური` returns nothing. The suite therefore runs in
**Georgian, English and Russian**. Language is a `DataProvider` parameter rather than three copies
of each test, so adding a locale is one entry in the `Language` enum and no test changes.

**Headless.** Off by default. The site is behind Cloudflare, whose bot check answers headless
Chrome with a challenge page instead of the application. `-Dheadless=true` still works wherever
that check does not apply.

**Waits.** No implicit wait is configured anywhere, and none should be added — implicit and
explicit waits do not compose. Everything waits through `WaitUtils`, which is built on
`FluentWait` throughout, and deliberately so: every wait in this suite needs either a custom
ignore set (clicking through `ElementClickInterceptedException` while Angular settles) or a
custom predicate (exact-match localised text after trimming, which `textToBePresentInElement`
does not do — it matches substrings and does not trim). `WebDriverWait` is a `FluentWait` with a
fixed set of conditions, so it would add a second idiom without adding capability. This also
keeps the negative assertions fast: `findElements` returns immediately, so proving an element is
*absent* costs nothing rather than burning a full timeout.

**Localisation timing.** Switching language re-renders the form, and the switcher button picks up
its new label before Angular finishes re-translating the fields. Assertions on localised copy wait
for the expected string rather than reading whatever is present at that instant.

**Whitespace.** The form does not trim. Whitespace-only input clears client-side validation and is
sent to the server, which refuses it — so that case expects the server toast, not a field error.

---

## Parallel execution

`parallel="methods"` with a per-thread browser; the driver lives in a `ThreadLocal` and every
thread gets its own Chrome, which matters because the selected language persists in
`localStorage`.

Two threads is the default because it measured fastest here:

| Threads | Wall clock |
|---|---|
| 1 | ~126s |
| 2 | ~90s |
| 3 | ~230s |

Three is not a typo — a third Chrome exhausts memory on this machine and the run thrashes. Tune
`-Dthreads` to the machine. It is also worth keeping modest against a production login.
