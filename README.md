# periplus-cart-test

A single end-to-end test for the Periplus (https://www.periplus.com) shopping cart, written in **Java 17 + Selenium 4 + TestNG** and organized with the **Page Object Model**.

**Scope:** log in → search for a product → add the first in-stock result to the cart → verify the cart reflects the addition.

## Prerequisites

- **JDK 17** (or newer)
- **Maven 3.9+**
- **Google Chrome** installed (Selenium Manager auto-resolves the matching `chromedriver`)

## Project layout

```
periplus-cart-test/
├── pom.xml
├── testng.xml
├── docs/
│   └── test-cases.md          # Test case table (Feature / Scenario / Expected / Status …)
└── src/test/
    ├── java/com/periplus/
    │   ├── base/BaseTest.java
    │   ├── pages/             # Page Objects: Home, SearchResults, Product, Cart, Login
    │   ├── tests/             # LoginAddToCartTest
    │   └── utils/             # DriverFactory, ConfigReader
    └── resources/config.properties
```

## Running the test

```bash
# headed Chrome (default)
mvn test

# headless
mvn test -Dheadless=true

# override the search keyword used as test data
mvn test -DsearchKeyword="harry potter"
```

## Credentials

The test requires a registered Periplus account. Credentials are read from a local `.env` file (preferred) or OS environment variables. If neither is set, the test is **skipped** (not failed).

1. Copy the template:
   ```powershell
   Copy-Item .env.example .env
   ```
2. Edit `.env` and fill in your Periplus credentials:
   ```
   PERIPLUS_EMAIL=you@example.com
   PERIPLUS_PASSWORD=your-password
   ```
3. Run the suite normally:
   ```powershell
   mvn test
   ```

`.env` is git-ignored (see [.gitignore](.gitignore)) — it will not be committed. Resolution order is **`.env` → OS env vars → empty** (see [ConfigReader#env](src/test/java/com/periplus/utils/ConfigReader.java#L51-L57)).

## Test case document

See [docs/test-cases.md](docs/test-cases.md) for the Feature / Scenario / Expected / Actual / Status / Severity / Comments table. After running the suite, fill in the **Actual Result** and **Status** columns.
