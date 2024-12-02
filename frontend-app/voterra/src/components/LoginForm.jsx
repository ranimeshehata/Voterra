/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { Col } from 'react-bootstrap';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';
import ContinueSep from './ContinueSep';
import mailIcon from '../assets/mail-icon.svg';
import eyeOffIcon from '../assets/eye-off-icon.svg';
import eyeIcon from '../assets/eye-icon.svg';
import useFetch from '../hooks/useFetch';
import { useRecoilState } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';

function LoginForm() {
  const validationErrors = {};
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loginError, setLoginError] = useState('');
  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const { post } = useFetch();

  const navigate = useNavigate();
  const [user, setUser] = useRecoilState(userState);
  const [isAuthenticated, setIsAuthenticated] = useRecoilState(isAuthenticatedState);

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
        console.log(response);
      } else {
          console.log(err);
          if (!err.status) {
            setLoginError('No server response.');
          } else if (err.status === 400) {
            setLoginError('Invalid Email or Password.');
          } else {
            setLoginError('Login failed. Please try again.');
          }
      }
    });
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
    <div className="w-full max-w-md bg-white p-8 shadow-lg rounded-lg ">
      <div className="flex flex-col gap-10 w-full">
        <div className="form-title">
          <h2 className="form-title-welcome">Welcome Back!</h2>
          <p className="form-title-guide">Enter your credentials to access your account</p>
        </div>

        <form className="login-form">
          <Col className="relative mb-5 inputField">
            <label htmlFor="email" className="emailLabel">
              Email
            </label>
            <div className="flex items-center border rounded-lg">
              <input
                className={`email-input ${errors.email || errors.password || loginError ? 'errorBorder' : ''}`}
                id="email"
                type="text"
                placeholder="Enter email"
                value={email}
                onChange={e => setEmail(e.target.value)}
              />
              <img src={mailIcon} alt="mail-icon" className="h-5 w-5 cursor-pointer mx-2" />
            </div>
            {errors.email && <p className="errorMsg">{errors.email}</p>}
          </Col>

          <Col className="relative mb-5 inputField">
            <label htmlFor="password" className="passwordLabel">
              Password
            </label>
            <div className="flex items-center border rounded-lg">
              <input
                className={`password-input ${
                  (!errors.email || (errors.email && errors.password)) && (errors.email || errors.password || loginError)
                    ? 'errorBorder'
                    : ''
                }`}
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
            <div className="text-right mt-2">
              <a href="#" className="forgot-password">
                Forgot Password?
              </a>
            </div>
            {errors.password && <p className="errorMsg">{errors.password}</p>}
            {loginError && <p className="errorMsg">{loginError}</p>}
          </Col>
          <Col className="mb-4">
            <button className="loginButton" type="submit" onClick={handleSubmit}>
              Login
            </button>
          </Col>
        </form>
        <ContinueSep />
        <div className="btns flex justify-between w-full">
          <button
            onClick={async () => {
              // let data=await authUsingProv(1);
              // signUpProv(data);
            }}
            className="w-2/5 p-4 shadow-lg rounded-lg"
          >
            <i className="fa-brands fa-google"></i> Google
          </button>
          <button
            onClick={async () => {
              // let data=await authUsingProv(0);
              // signUpProv(data);
            }}
            className="w-2/5 p-4 shadow-lg rounded-lg"
          >
            <i className="fa-brands fa-facebook-f"></i> Facebook
          </button>
        </div>
        <p className="mt-6 text-center text-sm">
          Don’t have an account? <a href="/signup">Sign up</a>
        </p>
      </div>
    </div>
  );
}

export default LoginForm;
