import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

import FilePrintQueue from './components/FilePrintQueue';

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="App">
      <FilePrintQueue />
    </div>
  )
}

export default App
