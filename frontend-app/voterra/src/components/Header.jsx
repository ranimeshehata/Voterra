/* eslint-disable no-unused-vars */
import { useRecoilState } from 'recoil';
import Logo from '../assets/Logo.svg';
import { useLocation } from 'react-router-dom';
import { userState } from '../recoil/atoms';
import UserMenu from './UserMenu';
import SearchBar from './SearchBar';

function Header() {
  const [user, setUser] = useRecoilState(userState);
  const loc = useLocation();
  
  return (
    <div className="flex justify-center items-center relative h-20">
      <img src={Logo} className='absolute left-3' alt="Vottera Logo" style={{ width: '150px', height: 'auto' }} />
      {(loc.pathname !== "/login" && loc.pathname !== "/signup") && <SearchBar />}
    </div>
  );
}

export default Header;
