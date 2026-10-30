# SharpQRLauncher（QR 掃描）

點一下桌面圖示或小工具，就直接開啟 Sharp 手機內建的 QR 掃描 app
（`jp.co.sharp.android.qrreader/.MainActivity`），不顯示任何中間畫面。

## 需求
- Android 9（API 28）以上
- 手機需已安裝 Sharp 內建 QR 掃描 app（套件 `jp.co.sharp.android.qrreader`）。
  若找不到，會顯示「找不到 QR 掃描 app」。

## 安裝
到 [Releases](https://github.com/toober77/SharpQRLauncher/releases) 下載 `SharpQRLauncher-v1.0.3.apk` 安裝（需允許「安裝不明來源應用程式」）。

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

## 防閃爍黑幕

到「設定 → 應用程式 → QR 掃描 → 顯示在其他應用程式上層」開啟（或長按 app 圖示 →「設定黑幕權限」）。
之後點圖示／小工具會先蓋全螢幕黑幕約 0.3 秒，再開啟 Sharp QR，遮住切換那一幀。沒權限時直接開啟 QR 掃描，不會跳任何對話框。

## 加「直開 QR」捷徑（v1.0.3）

這個捷徑（標籤「QR 掃描」）會直接開 Sharp QR，不經過本 app，可以拖進資料夾。兩種加法：

1. **長按 app 圖示** → 選「加 QR 捷徑」→ 在桌面確認視窗按「新增」。
   長按選單還有「開啟 QR 掃描」與「設定黑幕權限」。
2. **小工具「加 QR 捷徑」**：長按桌面 →「小工具」→ 找到「加 QR 捷徑」（1x1，右上有 +）拖到桌面，點一下即跳出新增捷徑確認。加完可移除該小工具。

（v1.0.2 的首次設定對話框已移除。）
