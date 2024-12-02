
import { useRef, useState } from "react";
import useFetch from "../hooks/useFetch";
import { authUsingProv, sendOtp, validateForm } from "../voterraUtils/formUtils";
import SignUpHeader from "./SignupHeader";
import ContinueSep from "./ContinueSep";
const SignupForm = () => {
    const {data,error,loading,post,get}=useFetch();
    const [otpDone,setOtpDone]=useState(true);
    const [showOtp,setShowOpt]=useState(false);
    const [otp,setOpt]=useState(0);
    const [errors, setErrors] = useState({});
    let otpRef=useRef(null);
    const [formData,setFormData]=useState({email:"",password:"",username:"",firstName:"",lastName:"",gender:"MALE",userType:"USER",dateOfBirth:"",})
    function handleChange(e){
        setFormData({...formData,[e.target.name]:e.target.value});
    }
    const generateOtp = () => {
        return Math.floor(100000 + Math.random() * 900000);
    };
    function signUpProv(userObj){
        if(!userObj || !userObj.email){
            return
        }
        let formatted={email:userObj.email, password:"",username:"",firstName:userObj.firstName,lastName:"",gender:"",userType:"USER",dateOfBirth:"",}
        setFormData(formatted);
        console.log(formatted);
        
        post("http://localhost:8080/users/signup",formatted);
        console.log(data);
        
    }
    function verify(){
        let temp=generateOtp();
        setOpt(temp);
        console.log(formData.email);
        sendOtp(temp,formData.email);
    }
    function checkOtp(){
        setOtpDone(otp==otpRef.current.value);
    }
    function handleSubmit(){
        let res=validateForm(formData);        
        if(!res.valid){
            setErrors(res.errors);
            return;
        }
        if(otpDone){
            console.log(res);
            if(res.valid){
                console.log(formData);
                
                post("http://localhost:8080/users/signup",formData,(res)=>{
                    console.log(res);
                    
                });
                
            }
            else{
                setErrors(res.errors);
            }
        }
        else{
            setShowOpt(true);
            setErrors({});
            verify();
        }
    }
    return ( 
        <div className="w-[90%]  md:w-[50%] lg:w-[30%] bg-white rounded-lg shadow-2xl p-8 font-Roboto ">
            <SignUpHeader/>
            <div className="flex flex-col gap-10 w-full">
                
                <div className="flex items-center justify-between">
                    <div>
                        <h3 className="mb-2">First Name</h3>
                        <input placeholder="Enter first name"  type="text" onChange={handleChange} value={formData.firstName} name="firstName" className={`w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.firstName ? "border-red-500" : ""}`}/>

                    </div>
                    <div>
                        <h3 className="mb-2">Last Name</h3>
                        <input placeholder="Enter last name"  type="tex" onChange={handleChange} value={formData.lastName} name="lastName" className={`w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.lastName ? "border-red-500" : ""}`}/>
                    </div>
                </div>
                <div className="w-full">
                    <h3 className="mb-2">Gender</h3>
                    <select className="w-full border-2 border-gray-300 rounded-lg" onChange={handleChange} value={formData.gender} name="gender" id="">
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                </div>
                <div className="w-full">
                    <h3 className="mb-2">Birth date</h3>
                    <input name="dateOfBirth" value={formData.dateOfBirth} onChange={handleChange} className={`border-2 border-gray-300 rounded-lg w-full ${errors.dateOfBirth ? "border-red-500" : ""}`} type="date" />
                </div>
                <div className="">
                    <h3 className="mb-2">Username</h3>
                    <input placeholder="Enter username"  type="text" onChange={handleChange} value={formData.username} name="username" className={`w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.username ? "border-red-500" : ""}`} />
                </div>
                <div className="">
                    <h3 className="mb-2">Email</h3>
                    <input placeholder="Enter email"  type="email" onChange={handleChange} value={formData.email} name="email" className={`w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.email ? "border-red-500" : ""}`} />
                </div>
                <div className="">
                    <h3 className="mb-2">Password</h3>
                    <input placeholder="Enter password"  type="password" onChange={handleChange} value={formData.password} name="password" className={`w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.password ? "border-red-500" : ""}`}/>
                </div>
                
                <div className={`${showOtp?'max-h-[500px]':'max-h-0'} transition-all ease-in-out duration-1000 overflow-hidden`}>
                    <h3 className="mb-2">Email Otp</h3>
                    <div className="flex justify-between">
                        <input ref={otpRef} placeholder="Enter Otp sent to your email"  type="password" className="w-[90%] border-2 rounded-lg h-8 text-sm p-2 " />
                        <button className={`${otpDone?'bg-green-600 p-1 rounded-full':''} h-7 w-7 shadow-xl flex justify-center items-center`} onClick={checkOtp}><i className="fa-solid fa-check"></i></button>
                    </div>
                </div>
                <button onClick={handleSubmit} className="loginButton">
                    Sign up
                </button>
                <ContinueSep/>
                <div className="flex justify-between w-full">
                    <button onClick={async()=>{
                        let data=await authUsingProv(1);
                        signUpProv(data);
                    }} className="w-2/5 p-4 shadow-lg rounded-lg"><i className="fa-brands fa-google"></i>  Google</button>
                    <button onClick={async()=>{
                        let data=await authUsingProv(0);
                        signUpProv(data);
                    }} className="w-2/5 p-4 shadow-lg rounded-lg"><i className="fa-brands fa-facebook-f"></i>  Facebook</button>
                </div>

            </div>
        </div>
     );
}
 
export default SignupForm;