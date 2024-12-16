/* eslint-disable no-unused-vars */
import { useNavigate } from "react-router-dom";
const UserMenu = () => {
    const navigate=useNavigate();
    return ( 
        <div className="group relative md:hidden">
            <i className="fa-solid fa-user fa-xl"></i>
            <div className="z-10 w-32 shadow-lg flex flex-col justify-around absolute h-0 rounded-lg group-hover:h-56 group-hover:p-2 right-0 overflow-hidden transition-all ease-in-out duration-300 bg-white ">
                <p className="hover:bg-[#f0f0f0] w-full p-2 cursor-pointer rounded-xl">Profile</p>
                <p className="hover:bg-[#f0f0f0] w-full p-2 cursor-pointer rounded-xl">Saved posts</p>
                <p className="hover:bg-[#f0f0f0] w-full p-2 cursor-pointer rounded-xl">Logout</p>
            </div>
        </div>
     );
}
 
export default UserMenu;