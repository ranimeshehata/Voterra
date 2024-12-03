import { useState } from 'react';
import { Col } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import useFetch from '../hooks/useFetch';
import eyeOffIcon from '../assets/eye-off-icon.svg';
import eyeIcon from '../assets/eye-icon.svg';

function ResetPasswordForm() {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const { post } = useFetch();
  const navigate = useNavigate();
  const location = useLocation();
  const email = location.state?.email || '';

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleValidation = () => {
    const validationErrors = {};
    let valid = true;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%?&]{8,}$/;
  
    if (!newPassword) {
      validationErrors.newPassword = 'New password is required.';
      valid = false;
    } else if (newPassword.length < 8) {
      validationErrors.newPassword = 'Password is too short.';
      valid = false;
    } else if (!passwordRegex.test(newPassword)) {
      validationErrors.newPassword = 'Invalid password format.';
      valid = false;
    }
  
    if (!confirmPassword) {
      validationErrors.confirmPassword = 'Confirm password is required.';
      valid = false;
    } else if (newPassword !== confirmPassword) {
      validationErrors.confirmPassword = 'Passwords do not match.';
      valid = false;
    }
  
    setErrors(validationErrors);
    return valid;
  };

  const handleSubmit = e => {
    e.preventDefault();
    const isValid = handleValidation();
    if (isValid) {
      post(
        'http://localhost:8080/users/forgetPassword',
        { email, password: newPassword },
        (response, err) => {
          if (response) {
            navigate('/login');
          } else {
            console.log(err);
          }
        },
        (err) => {
          console.log(err);
        }
      );
    }
  };

  return (
    <div className="w-full max-w-md bg-white p-8 shadow-lg rounded-lg ">
      <div className="flex flex-col gap-10 w-full">
        <div className="form-title">
          <h2 className="form-title-welcome">Reset Password</h2>
          <p className="text-gray-500">Enter your new password</p>
        </div>
        <form className="reset-password-form" onSubmit={handleSubmit}>
          <Col className="relative mb-5 inputField">
            <div className="flex items-center border rounded-lg">
              <input
                className={`password-input ${ errors.newPassword ? 'errorBorder' : '' }`}
                id="newPassword"
                type={showPassword ? 'text' : 'password'}
                placeholder="Enter New Password"
                value={newPassword}
                onChange={e => setNewPassword(e.target.value)}
                onCopy={e => e.preventDefault()}
                onCut={e => e.preventDefault()}
              />
              <img
                src={showPassword ? eyeIcon : eyeOffIcon}
                alt="toggle visibility icon"
                className="h-5 w-5 cursor-pointer mx-2"
                onClick={togglePasswordVisibility}
              />
              {errors.newPassword &&
              <p className="errorMsg">
                {errors.newPassword}
              </p>
              }
            </div>
          </Col>
          <Col className="relative mb-5 inputField">
            <div className="flex items-center border rounded-lg">
              <input
                className={`password-input ${ errors.confirmPassword ? 'errorBorder' : '' }`}
                id="confirmPassword"
                type={showPassword ? 'text' : 'password'}
                placeholder="Confirm New Password"
                value={confirmPassword}
                onChange={e => setConfirmPassword(e.target.value)}
              />
              <img
                src={showPassword ? eyeIcon : eyeOffIcon}
                alt="toggle visibility icon"
                className="h-5 w-5 cursor-pointer mx-2"
                onClick={togglePasswordVisibility}
              />
              {errors.confirmPassword &&
              <p className="errorMsg">
                { errors.confirmPassword }
              </p>
              }
            </div>
          </Col>
          <button className="loginButton" type="submit">
            Reset
          </button>
        </form>
      </div>
    </div>
  );
}

export default ResetPasswordForm;
