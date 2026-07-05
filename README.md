# KycBank

An Android app for a digital banking platform where a relationship manager can browse customer accounts, check their KYC status, and verify pending customers by capturing a selfie right inside the app.

Built with Kotlin + Jetpack Compose, following a fairly standard clean architecture split (UI / domain / data), with Room for local caching and CameraX for the in-app selfie capture.

## What it does

There are two main screens:

**Accounts screen** — shows all customers split into Pending and Verified tabs. Each customer shows up as a card with their avatar, masked account number, balance, and KYC status. You can search by name or account number, and filter by account type (Savings / Current / NRI) using the chips at the top.

**Account details screen** — tapping a customer takes you here. Shows their full KYC profile (DOB, nationality, address, contact), account balance, and their bank + branch, which is resolved live from their IFSC code. If the customer is still Pending, there's a "Do KYC" flow that opens the in-app camera, captures a selfie, and flips them over to Verified.

The selfie capture is done entirely with CameraX — no system camera intent, no gallery picker. Camera permission is handled properly too, including the case where someone permanently denies it (in which case it points them to the app's Settings page instead of just showing a dead-end button).

## Why it's built this way

**Customer + bank data** comes from [DummyJSON](https://dummyjson.com)'s `/users` endpoint (name, avatar, DOB, address, bank details). DummyJSON doesn't return account balance or IFSC codes, so those are seeded locally — balance is randomly generated per customer, and IFSC is assigned from a small fixed list of real Indian bank codes.

**Bank/branch info** is resolved from that seeded IFSC using the [Razorpay IFSC API](https://ifsc.razorpay.com), which is free and needs no key.

**Caching** — both the customer list and IFSC lookups are cached locally with Room, with a time-based expiry (10 minutes for customers, 24 hours for IFSC since bank branch info basically never changes). This was mainly to keep things usable on a flaky connection, but it also means a customer's verified status and captured selfie survive an app restart — the local cache is the actual source of truth for KYC status, not just a network mirror.

One thing worth calling out: since the customer list gets refreshed from the network periodically, there was a real bug early on where a fresh fetch would silently overwrite anyone who'd already been verified locally (since the API obviously doesn't know about your local KYC actions). Fixed by merging the fresh network data with whatever KYC state already exists locally, instead of blindly replacing the row.

## Architecture

```
ui/          → Compose screens, ViewModels, UI state
domain/      → Models, repository interfaces, use cases
data/        → Repository implementations, Room DAOs/entities, Retrofit APIs, DTO mappers
di/          → Hilt modules (network, database, repository bindings)
```

Standard unidirectional flow: Screens observe `StateFlow` from ViewModels, ViewModels call use cases, use cases call repositories, repositories decide whether to serve from cache or hit the network.

## Tech stack

- Kotlin + Jetpack Compose
- Hilt (dependency injection)
- Retrofit + OkHttp (networking)
- Room (local caching)
- CameraX (in-app selfie capture)
- Coil (image loading)
- Kotlin Coroutines + Flow

## Running it

Clone the repo and open it in Android Studio (should work fine on a recent stable version). No API keys needed — both DummyJSON and the Razorpay IFSC API are free and open.

```
git clone https://github.com/thakur-027/android-kyc-assignment.git
```

Build and run on an emulator or physical device with API 24+. Camera capture obviously needs an actual camera — most emulators support a simulated one, but a physical device gives a much better test of the KYC flow.

## Screenshots

<img width="187" height="404" alt="sample_completed" src="https://github.com/user-attachments/assets/89b307ee-7161-4baa-919e-30ec18afd10e" />

<img width="186" height="401" alt="completed_kycs" src="https://github.com/user-attachments/assets/af001904-e453-4e95-8b3f-f9b7dfb8a987" />

<img width="187" height="404" alt="sample_kyc_acc" src="https://github.com/user-attachments/assets/860a0f66-ddbf-44c5-83e9-78352f7e6b53" />

<img width="186" height="405" alt="pending_kycs" src="https://github.com/user-attachments/assets/77ff4db8-a66a-4fce-b85f-0d37b3bfabea" />


## Demo Video

https://drive.google.com/file/d/1QBk0zMnOngnbjl6Hw2eFlSHWJD5DMoqQ/view?usp=sharing



## Known limitations / possible improvements

- IFSC codes are seeded from a small fixed list rather than being genuinely tied to each customer's actual bank — DummyJSON doesn't provide real Indian IFSC data, so this was the practical way to demo live IFSC resolution.
- Pagination is done at the rendering level (windowed list + infinite scroll) rather than true server-side paging, since DummyJSON returns its whole (small) user set in one call anyway.
- Theme preference (light/dark) resets on process death right now — would be a quick add to persist it with DataStore if needed.
- No automated tests yet beyond the default generated ones.
