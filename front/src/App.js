import logo from './logo.svg';
import './App.css';
import { Layout } from "./components";
import {ChatPage, ResultPage, MainPage, HistoryListPage} from "./pages";

function App() {
  return (
      <Layout>
        <ResultPage />
      </Layout>
  );
}


export default App;
