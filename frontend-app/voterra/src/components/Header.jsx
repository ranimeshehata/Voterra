import { useRecoilState } from 'recoil';
import Logo from '../assets/Logo.svg';
import { useNavigate } from 'react-router-dom';
import { userState } from '../recoil/atoms';
import UserMenu from './UserMenu';

function Header() {
  const navigate = useNavigate();
  const [user,setUser]=useRecoilState(userState);
  return (
    <div className="p-3 flex justify-between items-center px-4 sm:px-12">
      <img src={Logo} alt="Vottera Logo" style={{ width: '150px', height: 'auto' }}/>
        <UserMenu/> 
    </div>
    );
}

export default Header;
