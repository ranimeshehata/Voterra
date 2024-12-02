import emailjs from '@emailjs/browser';
import { GoogleAuthProvider, FacebookAuthProvider, signInWithPopup } from "firebase/auth";
import {auth} from '../auth/firebase';
export const validateForm = (formData) => {
    const newErrors = {};

    if (!formData.username.trim()) newErrors.username = "Username is required";
    if(!formData.gender.trim()) newErrors.gender = "Gender is required";
    if(!formData.firstName.trim()) newErrors.firstName = "First Name is required";
    if(!formData.lastName.trim()) newErrors.lastName = "Last Name is required";
    if(!formData.dateOfBirth) newErrors.dateOfBirth = "Date of birth is required";
    if (!formData.email.trim()) newErrors.email = "Email is required";
    else if (!/\S+@\S+\.\S+/.test(formData.email))
        newErrors.email = "Email is invalid";
    if (!formData.password.trim()) {
      newErrors.password = "Password is required";
  } else if (formData.password.length < 8) {
      newErrors.password = "Password must be at least 8 characters";
  } else if (!/[A-Z]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one uppercase letter";
  } else if (!/[a-z]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one lowercase letter";
  } else if (!/[^a-zA-Z0-9]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one special character";
  }
    return { valid: Object.keys(newErrors).length === 0, errors: newErrors };
};

export const sendOtp = (otpCode,email) => {
    const message = `Your verification code is ${otpCode}`;
    emailjs
      .send(
        'service_8j8esgb',
        'template_p1r3bcn',
        {
          to_email: email,
          message: message,
        },
        'VxOAgAMBAcXRsSXsb'
      )
      .then(
        (result) => {
          console.log('OTP sent successfully:', result.text);
        },
        (error) => {
          console.log('Failed to send OTP:', error.text);
        }
      );
};


export const authUsingProv = async (providerIndex) => {
  let provider;
  if (providerIndex === 1) {
      provider = new GoogleAuthProvider();
  } else if (providerIndex === 0) {
      provider = new FacebookAuthProvider();
  }
  try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;

      if (user) {
          let res={
            email: user.email,
            firstName: user.displayName,
        };
        console.log("User authenticated successfully:", res);
        return res;
      }
  } catch (error) {
      console.error("Error during authentication:", error.message);
  }
};