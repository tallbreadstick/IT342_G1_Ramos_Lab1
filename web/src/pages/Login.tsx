import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login, saveToken } from '../api/auth';
import type { LoginRequest } from '../api/auth';

export default function Login() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState<LoginRequest>({
        usernameOrEmail: '',
        password: '',
    });

    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            // Basic validation
            if (!formData.usernameOrEmail || !formData.password) {
                setError('Username/email and password are required');
                setLoading(false);
                return;
            }

            const response = await login(formData);
            saveToken(response.token);

            // Redirect to dashboard
            navigate('/dashboard');
        } catch (err: unknown) {
            const errorMessage =
                err instanceof Error ? err.message : 'Login failed. Please try again.';
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-[calc(100vh-4rem)] bg-white flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                <h1 className="text-2xl font-bold text-gray-900 mb-6">login</h1>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="usernameOrEmail" className="block text-sm text-gray-700 mb-1">
                            username or email
                        </label>
                        <input
                            id="usernameOrEmail"
                            name="usernameOrEmail"
                            type="text"
                            value={formData.usernameOrEmail}
                            onChange={handleChange}
                            className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-gray-900"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm text-gray-700 mb-1">
                            password
                        </label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                            className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-gray-900"
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full px-4 py-2 text-sm bg-gray-900 text-white hover:bg-gray-800 transition disabled:opacity-50"
                    >
                        {loading ? 'logging in...' : 'login'}
                    </button>
                </form>

                {error && (
                    <div className="mt-4 p-3 border border-red-300 bg-red-50">
                        <p className="text-sm text-red-700">{error}</p>
                    </div>
                )}

                <div className="mt-6 text-sm text-gray-600">
                    no account?{' '}
                    <button
                        onClick={() => navigate('/register')}
                        className="text-gray-900 underline hover:no-underline"
                    >
                        register
                    </button>
                </div>
            </div>
        </div>
    );
}
