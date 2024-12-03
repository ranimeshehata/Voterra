import { useEffect } from "react";
import Header from "../components/Header";
import SignupForm from "../components/SignupForm";

const SignUp = () => {
    return ( 
        <div className="">
            <div className="header">
                <Header/>
            </div>
            <div className="w-full flex justify-center">
                <SignupForm/>
            </div>

        </div>
     );
}
 
export default SignUp;