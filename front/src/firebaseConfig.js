import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
    apiKey: "AIzaSyB4NLoJXxtmhyqOxqMhVxI9ZyOPGHAOvTE",
    authDomain: "tp-is1.firebaseapp.com",
    projectId: "tp-is1",
    storageBucket: "tp-is1.appspot.com",
    messagingSenderId: "1041261405265",
    appId: "1:1041261405265:web:90bcb1e6a003c1d6e00470"
};

const app = initializeApp(firebaseConfig);
const storage = getStorage(app);

export { storage };