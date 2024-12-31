import Header from '../components/Header';
import logo from '../assets/Logo.svg';
import { useNavigate } from 'react-router-dom';

function ContactsPage() {
    const navigate = useNavigate();

    const handleBackToHomeClick = () => {
        navigate('/homepage');
    };

  return (
<div className="homepage-container bg-white-100 w-full absolute top-0">
        <div className="header">
            <Header />
        </div>
        <div className="about-page p-8">
            <div className="about-page-content max-w-4xl mx-auto shadow-2xl p-6 bg-white rounded-lg">
                <div className="logo-container text-center mb-8">
                    <img src={logo} alt="Voterra Logo" className="mx-auto w-50 h-50" />
                </div>
                
                <div className="list-disc list-inside mb-6">
                    <i className="fas fa-envelope fa-2x text-red-600 mr-4 ml-10">
                        <span className='text-red-600 lowercase ml-3 text-xl'>
                            voterra.app@gmail.com
                        </span>
                    </i>                                     
                </div>
                <div className="text-center mb-2">
                    <button 
                        type="submit"
                        className="bg-red-500 mb-5 text-white p-2 rounded-lg mt-2 shadow ml-40 mr-40 text-lg font-bold hover:bg-red-600"
                        onClick={handleBackToHomeClick}
                    >
                        Back to Home
                    </button>
                </div>
            </div>
        </div>
    </div>
  );
}

export default ContactsPage;