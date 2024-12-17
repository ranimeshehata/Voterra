import Logo from '../assets/Logo.svg';
import { useNavigate } from 'react-router-dom';

function UserProfileHeader() {
  const navigate = useNavigate();
  return (
    <div className="login-header">
      <img src={Logo} alt="Vottera Logo" style={{ width: '200px', height: 'auto' }}  />
        <div className='flex'>
         
        </div>    
    </div>
    );
}

export default UserProfileHeader;