The application is To-Do app and is still being developed.
On the first fragment you will see your current activity and time when that activity will finish.

You can add 2 types of activities, activities without scheduled time (start and finish time) and activities with scheduled time.
Both type of activities are displayed on the fragment right to the first one.

It is possible to erase and mark activity as done. If is activity done in expected time it will become green, if not it will become yellow.

Application is developed in MVVM architecture. ViewModel is using LiveData and views are observing data.
Database is made with Room library 
WorkManager is always working in the background and each day it saves data for previous days to another table, and delete it from "main table" in which all 
relevant data for Views is stored. It means that every day You work with brand new scheduled activities (not scheduled activities are not erased)

