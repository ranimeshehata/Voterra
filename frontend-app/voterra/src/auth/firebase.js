// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import {getAuth} from "firebase/auth";
import {getFirestore} from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyB7vcdgUmlsLYCqAWh4A-83vQ0qowjyxLE",
    authDomain: "voterra-c25c2.firebaseapp.com",
    projectId: "voterra-c25c2",
    storageBucket: "voterra-c25c2.firebasestorage.app",
    messagingSenderId: "908528803094",
    appId: "1:908528803094:web:d6944f9df56c5fe8418437"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

export const auth=getAuth();
export const db=getFirestore(app);
export default app;