Shyamal H Anadkat
////////////////////////////////////////////////////////////////////////////////////
Suppose you are now in Activity A. You click
“Dialog” and subsequently “Close”, what are the methods being called in sequence? 
////////////////////////////////////////////////////////////////////////////////////
Activity A.onCreate();
Activity A.onStart();
Activity A.onResume();
Activity A.onPause();
DialogActivity.onCreate();
DialogActivity.finishDialog(...);
Activity A.onResume();