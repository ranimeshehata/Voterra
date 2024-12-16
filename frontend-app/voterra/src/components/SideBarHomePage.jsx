import { Row, Col } from 'react-bootstrap';
import Avatar from 'react-avatar';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import PropTypes from 'prop-types';


function SideBarHomePage({ handleLogout }) {
  return (
    <div>
        <div className="profile">
            <div className="profile-pic">
                <Avatar
                
                size="40"
                className="avatar"
                round
                color="#B90000"
                boxShadow="0px 17px 40px 4px rgba(25, 30, 36, 0.11)"
                placeholder="V"
                />
            </div>
            <div  className="profile-name">
                <span className="profile-name-text" data-tooltip-id="profile-name-tooltip" >
                    
                </span>
            </div>
            <ReactTooltip id="profile-name-tooltip" />
        </div>
        <div className='sidebar-links'>
           
            <Row>
                <Col>
                    <a href="/profile" className="sidebar-link">
                        <i className="fas fa-user"></i>
                        <span className="sidebar-link-text">Profile</span>
                    </a>
                </Col>
            </Row>
                
      
            <Row>
                <Col>
                    <a href="/savedposts" className="sidebar-link">
                        <i className="fas fa-bookmark"></i>
                        <span className="sidebar-link-text">Saved Posts</span>
                    </a>
                </Col>
            </Row>
            <Row>
                <Col>
                    <a href="/about" className="sidebar-link">
                        <i className="fas fa-info-circle"></i>
                        <span className="sidebar-link-text">About</span>
                    </a>
                </Col>
            </Row>
            <Row>
                <Col>
                    <a href="/contact" className="sidebar-link">
                        <i className="fas fa-envelope"></i>
                        <span className="sidebar-link-text">Contact</span>
                    </a>
                </Col>
            </Row>
            <Row>
                <Col>
                    <a href="#"
                        onClick={handleLogout}
                        className="sidebar-link">
                        <i className="fas fa-sign-out-alt"></i>
                        <span className="sidebar-link-text">Sign Out</span>
                    </a>
                </Col>
            </Row>
            </div>
    </div>

  );
}


export default SideBarHomePage;