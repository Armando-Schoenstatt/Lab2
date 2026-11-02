import { useEffect, useMemo, useState } from 'react';

const API_URL = 'http://localhost:8080/api';

const connectionError =
  'Não foi possível conectar à API. Verifique se o backend está em execução em http://localhost:8080.';

function App() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [user, setUser] = useState(null);
  const [clients, setClients] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [tab, setTab] = useState('dashboard');

  const authHeaders = useMemo(
    () => ({
      'Content-Type': 'application/json',
      Authorization: `Basic ${window.btoa(`${username}:${password}`)}`,
    }),
    [username, password]
  );

  const loadData = async () => {
    if (!user) {
      return;
    }

    setLoading(true);
    try {
      const [clientsRes, vehiclesRes, requestsRes] = await Promise.all([
        fetch(`${API_URL}/clients`, { headers: authHeaders }),
        fetch(`${API_URL}/vehicles`, { headers: authHeaders }),
        fetch(`${API_URL}/rental-requests`, { headers: authHeaders }),
      ]);

      if (!clientsRes.ok || !vehiclesRes.ok || !requestsRes.ok) {
        throw new Error('Unable to load system data');
      }

      const [clientsData, vehiclesData, requestsData] = await Promise.all([
        clientsRes.json(),
        vehiclesRes.json(),
        requestsRes.json(),
      ]);

      setClients(clientsData);
      setVehicles(vehiclesData);
      setRequests(requestsData);
    } catch (error) {
      setMessage(error instanceof TypeError ? connectionError : error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user) {
      loadData();
    }
  }, [user]);

  const handleLogin = async (event) => {
    event.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Credenciais inválidas');
      }

      const data = await response.json();
      setUser(data);
      setMessage(`Bem-vindo, ${data.username}`);
    } catch (error) {
      setMessage(error instanceof TypeError ? connectionError : error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setUser(null);
    setClients([]);
    setVehicles([]);
    setRequests([]);
    setMessage('Sessão encerrada');
  };

  if (!user) {
    return (
      <div className="login-shell">
        <form className="login-card" onSubmit={handleLogin}>
          <h1>Car Rental System</h1>
          <label>
            Usuário
            <input value={username} onChange={(e) => setUsername(e.target.value)} />
          </label>
          <label>
            Senha
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </label>
          <button type="submit" disabled={loading}>{loading ? 'Entrando...' : 'Entrar'}</button>
          {message && <p className="message">{message}</p>}
        </form>
      </div>
    );
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <div>
          <h2>Sistema de Aluguel de Carros</h2>
          <p>Usuário autenticado: {user.username}</p>
        </div>
        <button className="secondary" onClick={handleLogout}>Sair</button>
      </header>

      <nav className="tabs">
        <button className={tab === 'dashboard' ? 'active' : ''} onClick={() => setTab('dashboard')}>Dashboard</button>
        <button className={tab === 'clients' ? 'active' : ''} onClick={() => setTab('clients')}>Clientes</button>
        <button className={tab === 'vehicles' ? 'active' : ''} onClick={() => setTab('vehicles')}>Veículos</button>
        <button className={tab === 'requests' ? 'active' : ''} onClick={() => setTab('requests')}>Pedidos</button>
      </nav>

      {message && <div className="banner">{message}</div>}

      {loading && <div className="loading">Carregando dados...</div>}

      {tab === 'dashboard' && (
        <section className="card-grid">
          <div className="card stat">
            <span>Clientes</span>
            <strong>{clients.length}</strong>
          </div>
          <div className="card stat">
            <span>Veículos</span>
            <strong>{vehicles.length}</strong>
          </div>
          <div className="card stat">
            <span>Pedidos</span>
            <strong>{requests.length}</strong>
          </div>
        </section>
      )}

      {tab === 'clients' && (
        <section className="panel">
          <h3>Clientes</h3>
          <table>
            <thead>
              <tr>
                <th>Nome</th>
                <th>CPF</th>
                <th>RG</th>
                <th>Profissão</th>
                <th>Renda</th>
              </tr>
            </thead>
            <tbody>
              {clients.map((client) => (
                <tr key={client.id}>
                  <td>{client.name}</td>
                  <td>{client.cpf}</td>
                  <td>{client.rg}</td>
                  <td>{client.occupation}</td>
                  <td>{client.income}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      )}

      {tab === 'vehicles' && (
        <section className="panel">
          <h3>Veículos</h3>
          <table>
            <thead>
              <tr>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Ano</th>
                <th>Placa</th>
                <th>Proprietário</th>
              </tr>
            </thead>
            <tbody>
              {vehicles.map((vehicle) => (
                <tr key={vehicle.id}>
                  <td>{vehicle.brand}</td>
                  <td>{vehicle.model}</td>
                  <td>{vehicle.manufactureYear}</td>
                  <td>{vehicle.plate}</td>
                  <td>{vehicle.ownerType}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      )}

      {tab === 'requests' && (
        <section className="panel">
          <h3>Pedidos de aluguel</h3>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Data de início</th>
                <th>Data de retorno</th>
                <th>Status</th>
                <th>Contrato</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((request) => (
                <tr key={request.id}>
                  <td>{request.id}</td>
                  <td>{request.rentalDate}</td>
                  <td>{request.returnDate}</td>
                  <td>{request.status}</td>
                  <td>{request.creditContractNumber || 'Sem contrato'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      )}
    </div>
  );
}

export default App;
