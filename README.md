# BeChef

BeChef 是一款學習烹飪的協助小幫手，可跟著 YouTube 影片一起做料理，也可記錄學習的食譜，甚至創造使用者自己的私房食譜。

## 如何使用

* User with APK
    * 下載 [APK](https://github.com/paulayan0000/BeChef/blob/master/app-debug.apk) 並於裝置中安裝。
    * 如欲觀看 YouTube 影片，裝置內需安裝 YouTube APP 並將之更新至最新版本。查詢或觀看多次 YouTube 影片後可能會因達每日 YouTube API 配額而無法使用，請隔日再次使用；若為開發者可依照 Developer with Code 設定 API ey 後進行後續開發及測試。

* Developer with Code
    * 更新 [ApiKey.java](https://github.com/paulayan0000/BeChef/blob/master/app/src/main/java/com/paula/android/bechef/ApiKey.java) 中 DEVELOPER_KEY。
    * 若無 API key 請至 Google Developer Console 進行申請，並設定開啟 YouTube Data API。

## 功能說明

### [登入頁面]

* 使用 Google 登入。

### [發現]

* 使用 ViewPager2 左右滑動或點擊 TabLayout 可檢視不同 YouTube 頻道的上傳影片，預設追蹤五個烹飪相關頻道。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545844-1b824700-b05a-11eb-9c85-875dea5cc891.png"></kbd> 可進入 YouTube 搜尋頁面，可依照指定關鍵字搜尋相關影片或頻道。點擊頻道搜索結果之項目後，可於其詳細頁面將此頻道加入追蹤清單。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545833-16bd9300-b05a-11eb-81a6-d394fd37736e.png"></kbd> 可刪除目前已加入的書籤。
* 點擊項目可進入詳細頁面，顯示此影片詳細內容。橫向模式影片自動顯示為全螢幕，因此如欲查看影片下方的資訊欄需將手機切換至直向模式。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545843-1a511a00-b05a-11eb-8999-af716a683f74.png"></kbd> 可重新載入此頻道之內容。

### [收藏]

* 使用 ViewPager2 左右滑動或點擊 TabLayout 可檢視不同書籤的收藏項目，預設提供一個空的未命名書籤。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545844-1b824700-b05a-11eb-9c85-875dea5cc891.png"></kbd> 可進入收藏資料庫篩選頁面，可依照指定關鍵字在指定條件下篩選符合的收藏。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545833-16bd9300-b05a-11eb-81a6-d394fd37736e.png"></kbd> 可新增、重新命名或刪除書籤。
    * 必須輸入書籤名稱才可儲存。
    * 注意刪除書籤時該書籤內的所有項目將全部刪除。
* 點擊單個項目右側的 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117546632-f1328880-b05d-11eb-86a9-772f3f9e7a76.png"></kbd> 可編輯項目內容。
* 點擊項目可進入詳細頁面，顯示此收藏詳細內容。此頁面中顯示的日期為加入收藏資料庫的日期，而非上傳者上傳至 YouTube 的日期。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545835-18875680-b05a-11eb-8c14-77c13a98508a.png"></kbd> 可改書籤顯示的排序方式，排序方式包含時間由新至舊、時間由舊至新、評分由高至低及評分由低至高。
* 長按項目可開啟選擇模式，選擇單個或多個項目後可進行移動、刪除或取消選取。
    * 如欲移動及刪除，則至少需選擇一個項目。
    * 取消選取可取消目前選取，並關閉選擇模式，點擊其他書籤亦可進行取消選取。

### [食譜]

* 同收藏之功能。
* 點擊 <kbd><img width="20" height="20" src="https://user-images.githubusercontent.com/40931901/117545841-1a511a00-b05a-11eb-9dc1-a95794e36e6f.png"></kbd> 可新增新的食譜。
    * 必須輸入標題且至少需包含一個步驟。
    * 相片來源可選擇相機、相簿或者移除原來相片。選擇相機需開啟拍照及存取外部儲存之權限。
    * 食材細項及步驟相片可上下拖曳以改變順序。

## Screenshot

<img src="https://user-images.githubusercontent.com/40931901/117545130-15d73200-b057-11eb-9e00-7c248d39153f.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545137-1b347c80-b057-11eb-85a0-9fa85da7d632.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545139-1bcd1300-b057-11eb-8ed4-ee2e44397655.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545142-1d96d680-b057-11eb-84de-388a4a60f29e.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545144-1f609a00-b057-11eb-876b-3f049561649e.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545148-21c2f400-b057-11eb-9be5-59761495ad0e.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545149-24bde480-b057-11eb-9ab5-02ac42040cc7.png" width="23%"> <img src="https://user-images.githubusercontent.com/40931901/117545152-25ef1180-b057-11eb-9615-037f71612f72.png" width="23%">


## Implemented

* Design Pattern
    * Object-Oriented Programming (OOP)
    * Model-View-Presenter (MVP)

* API
    * YouTube Data API
    * YouTube Player API

* Core Functions
    * Google Login

* User Interface
    * ViewPager2
    * TabLayout
    * BottomNavigation
    * Fragment
    * RecyclerView
    * AlertDialog
    * DialogFragment

* Other Third-Party tool
    * OkHttp3
    * Picasso
    * Room

## Requirement

* Android Studio 4.1.3+
* Android SDK 19+
* Gradle 4.1.3

## Version

* 1.0.1 : Modified UI and fixed some bugs.

## Contact

paula.yan.app@gmail.com
