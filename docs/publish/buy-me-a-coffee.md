# Buy Me a Coffee - Google Play Donations

This app includes an optional **Offrir un cafe** donation in Settings. Payments are handled by **Google Play Billing** and paid out to your linked Google Play merchant account.

## Product ID in code

The in-app product ID must match Google Play Console exactly:

```
buy_me_coffee
```

Defined in `app/src/main/java/com/lykimq_uyen/french_nationality/core/billing/DonationProductIds.kt`.

The product is a **consumable** one-time purchase so users can donate more than once.

## Google Play Console setup

### 1. Link a payments profile

1. Open [Google Play Console](https://play.google.com/console).
2. Go to **Settings** -> **Payments profile**.
3. Complete merchant setup so Google can pay you.

### 2. Create the in-app product

1. Open your app in Play Console.
2. Go to **Monetize** -> **Products** -> **In-app products**.
3. Click **Create product**.
4. Use these values:

| Field | Value |
|-------|-------|
| Product ID | `buy_me_coffee` |
| Name | `Offrir un cafe` (or similar) |
| Description | Voluntary support for Naturalisation FR |
| Product type | **Consumable** (one-time, repeatable) |
| Price | Your choice (for example EUR 2.99) |
| Status | **Active** |

### 3. Upload a signed release build

Billing only works when the app is installed from Google Play. A sideloaded debug APK is not enough.

Build a release bundle:

```bash
./gradlew bundleRelease
```

Upload this file to Play Console:

```
app/build/outputs/bundle/release/app-release.aab
```

Publish at least to **Internal testing** before testing donations.

Release signing requires `keystore.properties` in the project root (not committed to git).

### 4. Add testers

1. Play Console -> **Testing** -> **Internal testing**.
2. Add your Gmail address as a tester.
3. Open the opt-in link and install the app from Google Play.

### 5. Test the donation

1. Open the app from the Play Store testing track.
2. Go to **Parametres**.
3. Tap **Offrir un cafe**.
4. Complete the Google Play payment flow.

In testing tracks, you can use Google Play test payment methods.

### 6. Receive payouts

- Revenue appears in Play Console -> **Monetize** -> **Revenue**.
- Google pays out to your linked bank account on the usual Play payout schedule.
- Google applies its standard store commission.

## App behavior

- Settings shows a **Soutenir l'app** section.
- While the product is loading, a spinner is shown.
- When the product is available, the button shows the localized price from Google Play.
- If billing is unavailable or the product is not configured yet, a short message is shown instead.
- After a successful purchase, the user sees: **Merci pour ton soutien !**

## Important notes

- The donation button stays disabled until the product exists and is **Active** in Play Console.
- You cannot fully test billing with `make install` or a sideloaded APK alone.
- This is a voluntary tip. It does not unlock app features, which fits Google Play support-style purchases.
- To add more donation amounts later, create additional product IDs in Play Console and add them to `DonationProductIds.all` in code.

## Related files

- `app/src/main/java/com/lykimq_uyen/french_nationality/core/billing/DonationBillingController.kt`
- `app/src/main/java/com/lykimq_uyen/french_nationality/feature/settings/presentation/components/SettingsContent.kt`
- `app/build.gradle.kts` (Google Play Billing dependency)
