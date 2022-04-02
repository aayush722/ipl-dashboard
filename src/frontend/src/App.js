import './App.scss';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import { TeamPage } from './pages/TeamPage';
import { MatchPage } from './pages/MatchPage';
import {HomePage} from './pages/HomePage';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/teams/:teamName' element={<TeamPage/>} />
          <Route path='/teams/:teamName/matches/:year' element={<MatchPage/>} />
          <Route path='/' element={<HomePage />} ></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
