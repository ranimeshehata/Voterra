import Header from "../components/Header";
import ResetPasswordForm from "../components/ResetPasswordForm";

function ResetPasswordPage() {
    return (
        <div className="bg-white-100 w-full absolute top-0 ">
            <div className="header">
                <Header />
            </div>
            <div className="flex-grow flex items-center justify-center w-full ">
                <ResetPasswordForm />
            </div>
        </div>
    );
}

export default ResetPasswordPage;