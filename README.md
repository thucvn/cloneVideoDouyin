This project is used for download all videos of a douyin channel. Using selenium and chrome webdriver.\
- First you need to download Chrome Webdriver( choose your version that compatible with your browser).\
[Download chrome webdriver here.](https://chromedriver.chromium.org/downloads)

- Open Main class, config location of webdriver in your computer:\
System.setProperty(<font color="green">"webdriver.chrome.driver"</font>, 
<font color='red'>The location of driver here</font>);
- Set some attributes below:
- <font color="purple">CHANNEL_URL</font>( url of channel to download) 
- <font color="purple">savedLoc</font>( the location saved files)
- <font color="purple">THREAD_NUM</font>( the number of thread run to download videos)
- <font color="purple">MAX_DOWNLOAD_VIDEOS</font>( maximum videos to be downloaded, you can fix it equals 999999999 if you need to get all)
- Run the Main class, and solved captcha, close any popup in the opened browsers. 