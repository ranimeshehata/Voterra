/* eslint-disable no-unused-vars */
import { useState, useEffect, useRef } from 'react';
import { Col } from 'react-bootstrap';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';
import mailIcon from '../assets/mail-icon.svg';
import eyeOffIcon from '../assets/eye-off-icon.svg';
import eyeIcon from '../assets/eye-icon.svg';
import useFetch from '../hooks/useFetch';
import { authUsingProv, sendOtp, validateForm } from "../voterraUtils/formUtils";
import { useRecoilState } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';

function LoginForm() {
  const validationErrors = {};
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loginError, setLoginError] = useState('');
  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const [otpSent, setOtpSent] = useState(false);
  const [otpDone, setOtpDone] = useState(false);
  const [otp, setOtp] = useState(0);
  const [otpInput, setOtpInput] = useState('');
  const otpRef = useRef(null);
  const { post,error } = useFetch();
  const navigate = useNavigate();
  const [user, setUser] = useRecoilState(userState);
  const [isAuthenticated, setIsAuthenticated] = useRecoilState(isAuthenticatedState);
  const [formData, setFormData] = useState({ email: "", password: "" });

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  useEffect(() => {
    const handleStorageChange = event => {
      if (event.key === 'token' && event.newValue) {
        toast.info('Another user is already logged in!');
        navigate('/homepage');
      }
    };
    window.addEventListener('storage', handleStorageChange);
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, [navigate, setIsAuthenticated]);

  const validateEmail = inputEmail => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(inputEmail).toLowerCase());
  };

  const validatePassword = inputPassword => {
    const re = /^(?=.*[a-z])(?=.*\d)[a-z\d]{8,}$/;
    return true;
  };

  const handleValidation = () => {
    setLoginError('');
    let valid = true;
    if (!email) {
      validationErrors.email = 'This field cannot be empty.';
      valid = false;
    } else if (!validateEmail(email)) {
      validationErrors.email = 'Invalid Email Format.';
      valid = false;
    }

    if (!password) {
      validationErrors.password = 'This field cannot be empty.';
      valid = false;
    } else if (valid && !validatePassword(password)) {
      validationErrors.password = 'Invalid Email or Password.';
    }
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length === 0) {
      return true;
    }
    return false;
  };

  const generateOtp = () => {
    return Math.floor(100000 + Math.random() * 900000);
  };

  const handleSendOtp = () => {
    if (!email) {
      setErrors({ email: 'Email is required to send OTP.' });
      return;
    }
    const generatedOtp = generateOtp();
    setOtp(generatedOtp);
    sendOtp(generatedOtp, email);
    setOtpSent(true);
  };

  const handleVerifyOtp = () => {
    if (otpInput === otp.toString()) {
      setOtpDone(true);
      navigate('/resetpassword', { state: { email } });
    } else {
      toast.error('Invalid OTP. Please try again.');
    }
  };

  const resetLoginForm = () => {
    setEmail('');
    setPassword('');
    setLoginError('');
  };

  const callBackend = () => {
    post('http://localhost:8080/users/login', { email, password }, (response, err) => {
      if (response) {
        const token = response.token;
        if (token) {
          localStorage.setItem('token', token);
          localStorage.setItem('user', JSON.stringify(response.user));
        }        
        const user = response.user;
        setUser(user);
        setIsAuthenticated(true);
        navigate('/homepage');
        resetLoginForm();
      } else {
        if (!err.status) {
          setLoginError('No server response.');
        } else if (err.status === 400) {
          setLoginError('Invalid Email or Password.');
        } else {
          setLoginError('Login failed. Please try again.');
        }
      }
    },()=>{});
  };


  const handleSubmit = e => {
    e.preventDefault();
    if (localStorage.getItem('token')) {
      toast.info('Another user is already logged in!');
      return;
    }
    const isValid = handleValidation();
    if (isValid) {
      callBackend();
    }
  };

  return (
   <div className='flex justify-center items-center h-[100vh]'>
     <div className="w-full max-w-md bg-white p-8 shadow-lg rounded-lg ">
      <div className="flex flex-col gap-10 w-full">
        <div className="form-title">
          <h2 className="form-title-welcome">
            Welcome Back!
          </h2>
          <p className="text-gray-500">
            Enter your credentials to access your account
          </p>
        </div>
        {error&&
        <div>
            <p className='text-red-600'>
                {error}
            </p>
        </div>
        }
        <form className="login-form">
          <Col className="relative mb-5 inputField">
            <label
              htmlFor="email"
              className="emailLabel"
            >
              Email
            </label>
            <div className="flex items-center border rounded-lg">
              <input
                className={`email-input
                          ${
                            errors.email || errors.password || loginError
                            ? 'errorBorder' : ''}`
                          }
                id="email"
                type="text"
                placeholder="Enter email"
                value={email}
                onChange={e => setEmail(e.target.value)}
              />
              <img src={mailIcon} alt="mail-icon" className="h-5 w-5 cursor-pointer mx-2" />
            </div>
            {errors.email &&
            <p className="errorMsg">
              {errors.email}
            </p>
            }
          </Col>
          <Col className="relative mb-5 inputField">
            <label 
              htmlFor="password"
              className="passwordLabel"
            >
              Password
            </label>
            <div className="flex items-center border rounded-lg">
              <input
                className={`password-input 
                          ${(!errors.email || 
                            (errors.email && errors.password)) &&
                            (errors.email || errors.password || loginError) 
                            ? 'errorBorder' : ''}`
                            }
                id="password"
                type={showPassword ? 'text' : 'password'}
                placeholder="Enter password"
                onChange={e => setPassword(e.target.value)}
                onCopy={e => e.preventDefault()}
                onCut={e => e.preventDefault()}
              />
              <img
                src={showPassword ? eyeIcon : eyeOffIcon}
                alt="toggle visibility icon"
                className="h-5 w-5 cursor-pointer mx-2"
                onClick={togglePasswordVisibility}
              />
            </div>
            {errors.password &&
            <p className="errorMsg">
              { errors.password }
            </p>}
          </Col>
          <Col className="mb-4">
            <button 
              className="loginButton" 
              type="submit" 
              onClick = { handleSubmit }
            >
              Login
            </button>
          </Col>
        </form>
      </div>
    </div>
   </div>
  );
}

export default LoginForm;
