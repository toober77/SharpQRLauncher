# SharpQRLauncher（QR 掃描）

點一下桌面圖示或小工具，就直接開啟 Sharp 手機內建的 QR 掃描 app
（`jp.co.sharp.android.qrreader/.MainActivity`），不顯示任何中間畫面。

## 需求
- Android 9（API 28）以上
- 手機需已安裝 Sharp 內建 QR 掃描 app（套件 `jp.co.sharp.android.qrreader`）。
  若找不到，會顯示「找不到 QR 掃描 app」。

## 安裝
到 [Releases](https://github.com/toober77/SharpQRLauncher/releases) 下載 `SharpQRLauncher-v1.0.0.apk` 安裝（需允許「安裝不明來源應用程式」）。

## 加入桌面小工具
1. 長按桌面空白處 →「小工具」。
2. 找到「QR 掃描」，拖到桌面。
3. 可長按小工具拖曳邊框調整大小；中間圖示固定大小置中。
4. 點小工具即開啟 QR 掃描。

## 自行編譯
需要 JDK 17 與 Android SDK（platform 35）。
```sh
echo "sdk.dir=/path/to/android-sdk" > local.properties
./gradlew assembleRelease
```

## 簽章
`app/build.gradle.kts` 預設讀取 `/workspace/keys/sharpqrlauncher-signing.properties`
（可用環境變數 `SHARPQR_SIGNING_PROPS` 指定其他路徑），內容：
```
storeFile=/path/to/release.jks
storePassword=...
keyAlias=...
keyPassword=...
```
也可用 `SHARPQR_STORE_FILE`、`SHARPQR_STORE_PASSWORD`、`SHARPQR_KEY_ALIAS`、`SHARPQR_KEY_PASSWORD` 環境變數。
沒有簽章設定時 release APK 會是未簽章版本。keystore 請勿提交到 repo。
