import Header from "../components/Header";
import LoginForm from "../components/LoginForm";

function Login() {
    return (
        <div className="bg-gray-100 min-h-screen">
            <div className="header">
                <Header />
            </div>
            <div className="flex-grow flex items-center justify-center">
                <LoginForm />
            </div>
        </div>
    );
}

export default Login;