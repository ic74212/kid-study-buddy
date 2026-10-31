# 學習小夥伴（kid-study-buddy）

輔助小朋友學習各年級課程的 Android App，適用國小 1～6 年級。

## 功能

- **年級選擇**：1～6 年級，App 會記住上次的選擇
- **🔢 數學練習**：依年級出題，每次 10 題，用大數字鍵盤作答
  | 年級 | 題型 |
  |---|---|
  | 1 | 20 以內加減、二位數加一位數 |
  | 2 | 三位數加減、九九乘法 |
  | 3 | 四位數加減、乘法、除法 |
  | 4 | 多位數乘法、除法 |
  | 5 | 小數加減、四則運算 |
  | 6 | 小數乘除、括號運算 |
- **✖️ 九九乘法**：查看 2～9 的乘法表，並挑戰單一數字或混合測驗
- **🔤 英文單字卡**：看圖、看中文，點一下聽英文發音（使用手機內建語音）
- **📝 英文小測驗**：看圖選出正確的英文單字
- **🔟 英文數字 1～10**：每次 10 題、順序打亂，三種題型
  1. 聽中文、寫英文（聽到「三」，打出 three）
  2. 聽英文、寫數字（聽到 three，按出 3）
  3. 看數字、說英文（看到 3 three 三，用麥克風念出來，語音辨識判斷對錯，每題 3 次機會）
- **🏆 成績畫面**：依答對題數給星星和鼓勵

## 下載 APK

每次推送程式碼，GitHub Actions 都會自動編譯，並更新到 Releases 的「最新版 APK」：

👉 https://github.com/ic74212/kid-study-buddy/releases/latest/download/kid-study-buddy.apk

用手機打開上面的連結下載後安裝（第一次需要允許「安裝未知來源的應用程式」）。

## 網頁版（平板、iPad、電腦）

功能跟 APK 一樣，用瀏覽器打開就能用：

👉 https://ic74212.github.io/kid-study-buddy/

在 iPad / 平板上可以按「分享 → 加入主畫面」，就會像 App 一樣出現在桌面。
網頁版的程式在 `web/index.html`，推送到 `main` 分支後會自動更新到 GitHub Pages。

## 開發

- Kotlin + Jetpack Compose，minSdk 26（Android 8.0）
- 自行編譯：`./gradlew assembleDebug`，APK 會在 `app/build/outputs/apk/debug/`
