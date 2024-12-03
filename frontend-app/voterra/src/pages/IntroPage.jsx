import Logo from "../assets/Logo.svg";
import { useNavigate } from 'react-router-dom';

function IntroPage() {
    const navigate = useNavigate();

    return(
        <div className="intro-container">
            <img
                src={Logo}
                alt ="Vottera Logo"
                style =
                {{ width: '600px', height: 'auto'}} 
            />
            <div>
                <button
                    className="signup-btn-intro"
                    onClick={() => navigate('/signup')}
                >
                    New to Voterra
                </button>
                <br />
                <button
                    className="login-btn-intro"
                    onClick={() => navigate('/login')}
                >
                    Login            
                </button>
            </div>
        </div>
    )
}

export default IntroPage;
