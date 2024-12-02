import Header from "../components/Header";
import LoginForm from "../components/LoginForm";

function Login() {
    return (
        <div className="bg-gray-100 w-full absolute top-0 ">
            <div className="header">
                <Header />
            </div>
            <div className="flex-grow flex items-center justify-center w-full ">
                <LoginForm />
            </div>
        </div>
    );
}

export default Login;