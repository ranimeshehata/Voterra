import Header from '../components/Header';
import logo from '../assets/Logo.svg';

function AboutPage() {
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
                <h1 className="about-page-title text-4xl font-bold mb-4 text-gray-800">About Voterra</h1>
                <p className="about-page-text text-lg mb-6 ml-6 mr-6">
                    In today&apos;s digital age, users are seeking interactive platforms where they
                    can not only share content but also gather opinions through polls or votes. So, we present Voterra ...
                    <br />
                    Voterra  is a social media platform that allows users to share their thoughts and opinions on various topics. 
                    Users can create posts and vote on posts to express their views. 
                    Voterra aims to provide a platform for healthy discussions and debates on a wide range of topics 
                    while maintaining a sense of community.

                </p>
                <h2 className="about-page-title text-3xl font-bold mb-4 text-gray-800">Our Mission</h2>
                <p className="about-page-text text-lg mb-6 ml-6 mr-6">
                    Our mission is to foster a community where people can engage in meaningful conversations and share diverse perspectives. 
                    We believe in the power of dialogue to bring about positive change and understanding.
                </p>
                <h2 className="about-page-title text-2xl font-bold mb-4 text-gray-800">Contact Us</h2>
                <p className="about-page-text text-lg mb-6 ml-6 mr-6">
                    If you have any questions or feedback, feel free to reach out to us:
                </p>
                <div className="list-disc list-inside mb-6">
                    <i className="fas fa-envelope fa-2x text-red-600 mr-4 ml-10">
                        <span className='text-red-600 lowercase ml-3 text-xl'>
                            voterra.app@gmail.com
                        </span>
                    </i>                                     
                </div>
            </div>
        </div>
    </div>
  );
}

export default AboutPage;