import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import '../styles/Login.css';

function LoginPage() {
    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            if (!mail || !password) {
                Swal.fire({
                    icon: 'error',
                    title: 'Erreur',
                    text: 'Veuillez entrer votre email et mot de passe.',
                });
                return;
            }

            const response = await axios.post('http://localhost:8080/auth/signin', { mail, password });

            // Stocker les informations de l'utilisateur
            const role = response.data.role;
            localStorage.setItem('idUser', response.data.idUser);
            localStorage.setItem('token', response.data.jwt);
            localStorage.setItem('role', role); // Stocker le rôle dans le localStorage

            // Rediriger vers `/Room`
            navigate('/Room');
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Connexion échouée',
                text: 'Email ou mot de passe incorrect.',
            });
        }
    };


    const handleSignupClick = () => {
        navigate('/signup');
    };

    return (
        <div className="login-container">
            <div className="login-box">
                {/* Logo et Titre */}
                <div className="header-section">
                    <div className="logo-wrapper">
                        <img src="/logo-white.png" alt="Logo Master ISI" className="login-logo" />
                    </div>
                    <h1 className="login-title">Bienvenue sur Master ISI Chat</h1>
                    <p className="login-subtitle">Connectez-vous pour discuter et collaborer.</p>
                </div>

                {/* Formulaire */}
                <div className="form-section">
                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            placeholder="Entrez votre email"
                            value={mail}
                            onChange={(e) => setMail(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Mot de passe</label>
                        <input
                            type="password"
                            placeholder="Entrez votre mot de passe"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    <button className="login-btn" onClick={handleLogin}>
                        Connexion
                    </button>
                </div>

                {/* Footer */}
                <div className="footer-section">
                    <p>
                        Pas encore inscrit ?{' '}
                        <button onClick={handleSignupClick} className="signup-link">
                            Créer un compte
                        </button>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;