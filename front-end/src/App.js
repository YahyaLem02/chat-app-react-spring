import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider } from "@material-tailwind/react";
import Room from './pages/Room';
import LoginPage from './pages/Login';
import SignupPage from './pages/Signup';

function App() {
    return (
        <ThemeProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<LoginPage/>} />
                    <Route path="/Room" element={<Room/>} />
                    <Route path="/signup" element={ <SignupPage/>} />
                </Routes>
            </Router>
        </ThemeProvider>
    );
}

export default App;