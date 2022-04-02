import {react, useEffect, useState} from 'react';
import { TeamTile } from '../components/TeamTile';

import './HomePage.scss';


export const HomePage = () => {
    const [teams, setTeams] = useState([]);

    useEffect(
        () => {
            const getAllTeams = async () => {
                const response = await fetch("http://localhost:8080/team");
                const teams = await response.json();
                setTeams(teams);   
            }
            getAllTeams();
        }, []
    );

    if (!teams) {
        return (
            <h1>Teams Not Found</h1>
        );
    }


  return (
    <div className="HomePage">
      <div className='header-section'>
        <h1 className='app-name'>IPL DASHBOARD</h1>
      </div>
      <div className='team-grid'>
          {teams.map(team => <TeamTile teamName={team.teamName} />)}
      </div>

    </div>
  );
}
