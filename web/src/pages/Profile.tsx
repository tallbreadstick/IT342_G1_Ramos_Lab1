import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile, getToken } from '../api/auth';
import type { User } from '../api/auth';

export default function Profile() {
    const navigate = useNavigate();
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const token = getToken();
                if (!token) {
                    setError('No authentication token found. Please log in.');
                    setLoading(false);
                    return;
                }

                const profileData = await getProfile();
                setUser(profileData);
            } catch (err: unknown) {
                const errorMessage = err instanceof Error ? err.message : 'Failed to load profile';
                setError(errorMessage);
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, []);

    if (loading) {
        return (
            <div className="min-h-[calc(100vh-4rem)] bg-white flex items-center justify-center">
                <div className="text-center">
                    <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
                    <p className="mt-4 text-gray-700">loading...</p>
                </div>
            </div>
        );
    }

    if (error || !user) {
        return (
            <div className="min-h-[calc(100vh-4rem)] bg-white flex items-center justify-center p-4">
                <div className="max-w-md w-full">
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">profile</h1>
                    <div className="p-4 border border-gray-300 bg-gray-50 mb-4">
                        <p className="text-sm text-gray-700">{error || 'not logged in'}</p>
                    </div>
                    <button
                        onClick={() => navigate('/login')}
                        className="px-4 py-2 text-sm bg-gray-900 text-white hover:bg-gray-800 transition"
                    >
                        login
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-[calc(100vh-4rem)] bg-white">
            <div className="max-w-screen-2xl mx-auto p-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-8">profile</h1>

                <div className="content-card">
                    <div className="border-t border-gray-200 pt-6">
                        <table className="w-full text-left max-w-2xl">
                            <tbody className="divide-y divide-gray-200">
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500 w-48">user id</td>
                                    <td className="py-3 text-sm text-gray-900">{user.id}</td>
                                </tr>
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500">username</td>
                                    <td className="py-3 text-sm text-gray-900">{user.username}</td>
                                </tr>
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500">email</td>
                                    <td className="py-3 text-sm text-gray-900">{user.email}</td>
                                </tr>
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500">joined</td>
                                    <td className="py-3 text-sm text-gray-900">{new Date(user.dateJoined).toLocaleDateString()}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div className="mt-8 flex gap-4">
                        <button
                            onClick={() => navigate('/dashboard')}
                            className="px-4 py-2 text-sm bg-gray-900 text-white hover:bg-gray-800 transition"
                        >
                            dashboard
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
