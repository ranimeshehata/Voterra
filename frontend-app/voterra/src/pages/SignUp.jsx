import { useEffect } from "react";
import Header from "../components/Header";
import SignupForm from "../components/SignupForm";

const SignUp = () => {
    useEffect(()=>{
        let x=localStorage.getItem("user");
        console.log(JSON.parse(x));
        
    },[])
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