/* eslint-disable no-unused-vars */
import { Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import { useRecoilState } from 'recoil';
import { isAuthenticatedState } from '../recoil/atoms';
import error from '../assets/error.png';
import Header from '../components/Header';

function ErrorNotFound() {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useRecoilState(isAuthenticatedState);

  const handleBackToHomeClick = () => {
    if (isAuthenticated) {
      navigate('/homepage');
    } else {
      navigate('/login');
    }
  };
  
  return (
    <div className="homepage-container bg-white-100 w-full absolute top-0">
      <div className="header">
        <Header />
      </div>
      <div 
        className="error-page"
        style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
      >
        <Container className="d-flex align-items-center error-section">
          <Row className="d-flex ">
            <Col xs={12} className="error-image mb-1 d-flex justify-content-center mt-15">
              <img src={error} alt="ERROR 404" style={{ width: '500px', height: '500px' }}/>
            </Col>
            <Col xs={12} className="d-flex justify-content-center">
              <button 
                type="submit"
                className="bg-red-500 mb-5 text-white p-2 rounded-lg mt-2 shadow ml-40 mr-40 text-lg font-bold hover:bg-red-600"
                onClick={handleBackToHomeClick}
                >
                Back to Home
              </button>
            </Col>
          </Row>
        </Container>
      </div>
    </div>
  );
}

export default ErrorNotFound;