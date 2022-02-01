collection of activities
a page is an activity 

When you start app freshly
onCreate
onStart
onResume

When you click BACK button to go to desktop
onPause
onStop
onDestroy

When you click the app again
onCreate
onStart
onResume

Now this time we click HOME button to go to desktop
onPause
onStop
onSaveInstanceState

Now we open the app again
onRestart
onStart
onResume

Now we kill this app completely
onPause
onStop
onSaveInstanceState

- `onCreate` 和 `onRestart`不会同时出现
- `onDestroy`只有一定会有`onCreate`
