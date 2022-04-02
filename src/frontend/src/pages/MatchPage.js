import {react, useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import { MatchDetailCard } from '../components/MatchDetailCard';
import { MatchSmallCard } from '../components/MatchSmallCard';
import { YearSelector } from '../components/YearSelector';
import { Link } from 'react-router-dom';

import "./MatchPage.scss";

export const MatchPage = () => {

  const [matches, setMatches] = useState([]);
  const {teamName, year} = useParams();
  useEffect(
    () => {
      const fetchMatches = async () => {
        const response = await fetch(`http://localhost:8080/team/${teamName}/matches?year=${year}`);
        const data = await response.json();
        setMatches(data);

      }
      fetchMatches();
    }, [teamName, year]
  );

  return (
    <div className="MatchPage"> 
      <div className='year-selector'>
        <h3>Select Year</h3>
        <YearSelector teamName={teamName}/>
      </div>  
      
      <div> 
        <h1 className='page-heading'><Link to={`/teams/${teamName}`}>{teamName}</Link> matches in year {year}</h1>
        { matches.map(match => <MatchDetailCard teamName={teamName} match={match}/>) }
      </div>
    </div>
  );
}
