import { useState, useContext, useEffect } from 'react';
import { Col} from 'react-bootstrap';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import { UserContext } from '../context/UserContext';
import { useNavigate } from 'react-router-dom';
import mailIcon from '../assets/mail-icon.svg';
import eyeOffIcon from '../assets/eye-off-icon.svg';
import eyeIcon from '../assets/eye-icon.svg';

function LoginForm() {
    const validationErrors={};
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loginError, setLoginError] = useState('');
    const [errors, setErrors] = useState({});
    const [showPassword, setShowPassword] = useState(false);

    const navigate = useNavigate();
    const { setUser, login, setIsAuthenticated } = useContext(UserContext);

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
        return re.test(inputPassword);
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
    
        console.log('pswd', password)
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
        axios
          .post('http://localhost:8080/users/login', {
            email,
            password,
          })
          .then(response => {
            const token = response.data.token;
            if (token) {
              localStorage.setItem('token', token);
              localStorage.setItem('user', response.data);
            }
            const user = response.data;
            setUser(user);
            login();
            navigate('/homepage');
            resetLoginForm();
          })
          .catch(err => {
            if (!err.response) {
                setLoginError('No server response.');
              } else if (err.response.status === 400) {
                setLoginError('Invalid Email or Password.');
              } else {
                setLoginError('Login failed. Please try again.');
              }
          });
    }

      const handleSubmit = e => {
        e.preventDefault();
        if (localStorage.getItem('token')) {
          toast.info('Another user is already logged in!');
          return;
        }
        const isValid=handleValidation();
        if (isValid) {
          callBackend();
        }
      };
    

    return (
      <div className="flex justify-center items-center  form-container">
        <div className="w-full max-w-md bg-white p-8 shadow-lg rounded-lg">
            <div className="form-title">
                <h2 className="form-title-welcome">Welcome Back!</h2>
                <p className="form-title-guide">
                    Enter your credentials to access your account
                </p>
            </div>
          
          <form className="login-form">
            <Col className="relative mb-5 inputField">
                <label htmlFor="email" className="emailLabel">
                    Email 
                </label>
                <div className="flex items-center border rounded-lg">
                    <input
                        className={`email-input ${(errors.email || errors.password || loginError) ? 'errorBorder' : ''}`}
                        id="email"
                        type="text"
                        placeholder="Enter email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                    />
                    <img 
                        src={mailIcon} 
                        alt="mail-icon" 
                        className="h-5 w-5 cursor-pointer mx-2" 
                    />
                </div>
                {errors.email && (
                  <p className="errorMsg">
                    {errors.email}
                  </p>
                )}
            </Col>

            <Col className="relative mb-5 inputField">
                <label htmlFor="password" className="passwordLabel">
                    Password
                </label>
                <div className="flex items-center border rounded-lg">
                    <input
                        className={`password-input ${(!errors.email || (errors.email && errors.password)) && (errors.email || errors.password || loginError) ? 'errorBorder' : ''}`}
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
                {errors.password && (
                  <p className="errorMsg">
                    {errors.password}
                  </p>
                )}
                {loginError && (
                  <p className="errorMsg">{loginError}</p>
                )}
            </Col>
            <Col className="mb-4">
                <button
                    className="loginButton"
                    type="submit"
                    onClick={ handleSubmit }
                >
                    Login
                </button>
            </Col>
        </form>
        <hr />
        <div className="continue-text">
            OR CONTINUE WITH
        </div>

        <div className="btns flex justify-between">
            <button className="google-btn">Google</button>
            <button className="facebook-btn">Facebook</button>
        </div>
        <p className="mt-6 text-center text-sm">
            Don’t have an account?{' '}
            <a href="/signup">
                Sign up
            </a>
        </p>
        </div>
      </div>
    );
  }
  export default LoginForm;
  