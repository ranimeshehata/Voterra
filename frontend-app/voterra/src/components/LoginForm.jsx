import { useState } from 'react';
import { Col} from 'react-bootstrap';
import mailIcon from '../assets/mail-icon.svg';
import eyeOffIcon from '../assets/eye-off-icon.svg';
import eyeIcon from '../assets/eye-icon.svg';

function LoginForm() {
    const [showPassword, setShowPassword] = useState(false);

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    return (
      <div className="flex justify-center items-center min-h-screen form-container">
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
                        className="email-input"
                        id="email"
                        type="text"
                        placeholder="Enter email"
                    />
                    <img 
                        src={mailIcon} 
                        alt="mail-icon" 
                        className="h-5 w-5 cursor-pointer mx-2" 
                    />
                </div>
            </Col>

            <Col className="relative mb-5 inputField">
                <label htmlFor="password" className="passwordLabel">
                    Password
                </label>
                <div className="flex items-center border rounded-lg">
                    <input
                        className="password-input"
                        id="password"
                        type={showPassword ? 'text' : 'password'}
                        placeholder="Enter password"
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
            </Col>
            <Col className="mb-4">
                <button className="loginButton" type="submit">
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
  