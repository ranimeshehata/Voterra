import Logo from '../assets/Logo.svg';
import { useNavigate } from 'react-router-dom';


function Header() {

    const navigate = useNavigate();

    return (
            <div className="login-header">
              <img src={Logo} alt="Vottera Logo" style={{ width: '200px', height: 'auto' }}  />
              <div className='flex'>
                <button
                  className="login-btn-header"
                  onClick={() => navigate('/login')}
                >
                  Log In
                </button>
                <button
                  className="signup-btn-header"
                  onClick={() => navigate('/signup')}
                >
                  Sign Up
                </button>
              </div>    
            </div>
    );
}

export default Header;