import './App.css';
import { Layout } from "./components";
import { ChatPage, ResultPage, MainPage, HistoryListPage, LoginPage } from "./pages";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/main" element={<MainPage />} />
                    <Route path="/result" element={<ResultPage />} />
                    <Route path="/chat" element={<ChatPage />} />
                    <Route path="/history" element={<HistoryListPage />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;
