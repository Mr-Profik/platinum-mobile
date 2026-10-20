# Platinum Mobile Launcher

Landscape Android launcher concept styled to match the Platinum Mobile design: black panels, orange accents, crown branding, city artwork, login/register pages, server card, progress indicator and news footer.

## Server values
- Game server IP: `80.242.59.112`
- Game server port: `3993`

## API connection — configure to your real backend
The launcher currently assumes the API is HTTP on `http://80.242.59.112:3993` and calls:
- `GET /` to check HTTP reachability
- `POST /api/login`
- `POST /api/register`

JSON body:
```json
{"nickname":"player","password":"password"}
```
Expected success JSON:
```json
{"success":true,"message":"Welcome"}
```

**These API routes are assumptions, not verified server endpoints.** A CRMP game port often speaks a game protocol rather than HTTP. If your API has a different host/port, HTTPS, paths, or JSON field names, edit `app/src/main/java/com/platinum/mobile/ApiClient.kt`. Until those match the backend, the app UI builds but authentication will not work. Do not place admin credentials or secret API keys in the APK.

## Build the APK through GitHub Actions
1. Create a GitHub repository.
2. Upload the contents of this ZIP (the files inside the project folder, not the ZIP itself).
3. Open **Actions** and enable workflows if GitHub asks.
4. The workflow runs automatically after a push, or use **Actions → Build Platinum Mobile APK → Run workflow**.
5. Open the completed run and download the `PlatinumMobile-debug-apk` artifact.

## Build locally
Open in Android Studio and run **Build → Build APK(s)**, or run:
```bash
gradle assembleDebug
```
