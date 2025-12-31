import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { LaboreoProvider } from './context/LaboreoContext';
import MainLayout from './components/layout/MainLayout';
import theme from './theme';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <LaboreoProvider>
        <MainLayout />
      </LaboreoProvider>
    </ThemeProvider>
  );
}

export default App;

