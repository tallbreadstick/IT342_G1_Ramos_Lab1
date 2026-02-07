import { Link, useNavigate, useLocation } from 'react-router-dom';
import { isAuthenticated, logout, logoutServer } from '../api/auth';

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const authenticated = isAuthenticated();

    const handleLogout = async () => {
        try {
            await logoutServer();
        } catch (err) {
            console.error('Logout failed:', err);
        } finally {
            logout();
            navigate('/login');
        }
    };

    const isActive = (path: string) => {
        return location.pathname === path;
    };

    return (
        <nav className="bg-white border-b border-gray-300">
            <div className="max-w-screen-2xl mx-auto px-8">
                <div className="flex justify-between items-center h-14">
                    <Link to="/" className="text-lg font-bold text-gray-900">
                        auth
                    </Link>

                    <div className="flex items-center gap-6">
                        {authenticated ? (
                            <>
                                <Link
                                    to="/dashboard"
                                    className={`text-sm hover:underline ${isActive('/dashboard') ? 'underline' : ''
                                        }`}
                                >
                                    dashboard
                                </Link>
                                <button
                                    onClick={handleLogout}
                                    className="text-sm hover:underline"
                                >
                                    logout
                                </button>
                            </>
                        ) : (
                            <>
                                <Link
                                    to="/login"
                                    className={`text-sm hover:underline ${isActive('/login') ? 'underline' : ''
                                        }`}
                                >
                                    login
                                </Link>
                                <Link
                                    to="/register"
                                    className={`text-sm hover:underline ${isActive('/register') ? 'underline' : ''
                                        }`}
                                >
                                    register
                                </Link>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
}
