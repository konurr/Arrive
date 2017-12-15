# Arrive
*Final Year Mobile Technologies Project*

This project has been developed in order to prevent **you** from ever *sleeping in* again.
That's no more:
>+ ~~Missing the bus/train~~
>+ ~~Being late for the lecture~~
>+ ~~Being late for work~~
>+ ~~Being late for the meeting~~

## Class Breakdown:
### java/com/arrive/com/conor/arrive/Silencers/
This package contains three classes each with unique means of achieving the same goal - silencing the alarms.
#### SilencerMathSums:
This class generates random numbers which are used to create 3 relatively simple questions, when these questions are answered correctly the alarm will stop.
#### SilencerScanABarcode:
This class creates a SurfaceView (with camera as source) with use of BarcodeDetector which scans the surfaceView for barcodes. When a barcode is found it will silence the alarm.
#### SilencerShakeToWake:
This class uses the phone's accelerometer to detect the user shaking the phone. When the phone is shaken 10 times the alarm will stop. 

### java/com/arrive/conor/arrive/
The classes located within this section either relate to the activities being displayed or background services to control the alarm.
#### AlarmReceiver
Responsible for initiating the alarm process. The class receives the broadcast message from the pending intent which contains information regarding the alarm. This class then creates an intent to the RingtoneService class which is responsible for playing the audio. 
#### Create Alarm Activity
This class is responsible for providing the user with the interface to create the alarm. 
#### CreateAlarmFABFragment
This class is what controls the landing page of the app. It will display the next alarm time if one is set and instructions if one is not.
#### HistoryFragment
Displays the most recent time to silence alarm if there is one, instructions if not.
#### InputDestinationFragment
Small fragment class used for receiving user input for destination. 
#### MainActivity
Main class for controlling the bottom menu navigation menu.
#### NavigationActivity
Class used to show users destination on google maps, also responsible for initiating the navigation.
#### RingtoneService
Service class which starts/stops the audio for the alarm.
#### SettingsFragment
Fragment class for allowing use to set their preferred choices regarding different settings for the app, such as ringtone, destination, etc.
#### SilenceAlarmActivity
Controller class for launching appropriate silencing method when alarm starts.
#### SilenceAlarmFragment
Small Fragment Class used to display silencing methods to receive user's input regarding desired silencing method.


#### Done:
+ ✔ Create an alarm
+ ✔ Stop an alarm
+ ✔ Change alarm silencing method
+ ✔ Change alarm ringtone
+ ✔ Change navigation destination
+ ✔ Show destination on map
+ ✔ Allow user to change destination on map
+ ✔ Provide navigation to destination
+ ✔ Store and read users prefences
+ ✔ Show analysis of time to silence alarm (timer)

#### Not Done (Yet!):
+ ✘ Delete alarms
+ ✘ Adjust alarm based on traffic info
+ ✘ Smartwear companion app

**Further Development/Improvements:**
+ Make alarms repeatable
+ Cancel alarm