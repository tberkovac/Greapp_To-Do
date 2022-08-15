The application is To-Do app and is still being developed.
On the first fragment you will see your current activity and time when that activity is expected to be finished.
<p align="middle">
  <img src="https://user-images.githubusercontent.com/92406137/184719891-72ced0b7-c355-4ab7-a047-1d0c507a8dac.jpg" width=300px height=600px>
</p>

Depending if switch is checked or not user can add 2 types of activities, activities without scheduled time (start and finish time) and activities with scheduled time. It is not possible to have 2 scheduled activities at the same time (should be at least 1 minute difference between start of the second and end of the first one).

<p align="middle">
  <img src="https://user-images.githubusercontent.com/92406137/184719807-4b6e18cc-39d4-40b0-acf9-1824913053be.jpg" width=300px height=600px>
</p>

Both type of activities are displayed on the fragment right to the first one.

If user clicks on the expected end time of an activity in list, dialog with clock will be opened and user will be able to select new expectedEndTime
for that activity. Rest of the activities will be rescheduled as well keeping the same order.
<p align="middle">
  <img src="https://user-images.githubusercontent.com/92406137/184718824-70fe5962-7237-4d79-90a9-aa0ed24b25d8.jpg" width=300px height=600px>
  <img src="https://user-images.githubusercontent.com/92406137/184719741-a4ac6285-3639-4c98-808f-2e16a0821a69.jpg" width=300px height=600px>
</p>

It is possible to erase and mark activity as done. If is activity done in expected time it will become green, if not it will become yellow.
On the button below the list user can switch between scheduled and non-scheduled activities

Application is developed in MVVM architecture. ViewModel is using LiveData and views are observing data.

Database is made with Room library 

WorkManager is always working in the background and each day it saves data for previous days to another table, and delete it from "main table" in which all 
relevant data for Views is stored. It means that every day You work with brand new scheduled activities (not scheduled activities are not erased)

H.S.'s idea
