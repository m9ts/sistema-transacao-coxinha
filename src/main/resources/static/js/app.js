const API = 'http://localhost:8080';
const NOTAS = [2, 5, 10, 20, 50, 100, 200];

const CARDAPIO = [
    { sabor: 'frango', emoji: '🍗', preco: 8.0 },
    { sabor: 'carne', emoji: '🥩', preco: 10.00 },
    { sabor: 'costela', emoji: '🦴', preco: 10.00 },
    { sabor: 'frango especial', emoji: '⭐', preco: 10.0 },   // Decorator
    { sabor: 'carne especial', emoji: '🎯', preco: 11.0 }      // DescontoDecorator
];

let clienteLogado = null;
let notaSelecionada = null;

function toast(msg, tipo = 'info') {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = `show ${tipo}`;
    setTimeout(() => { t.className = ''; }, 3000);
}

function formatarMoeda(valor) {
    return parseFloat(valor).toLocaleString('pt-BR', { minimumFractionDigits: 2 });
}

function formatarData(dataISO) {
    if (!dataISO) return '—';
    const d = new Date(dataISO);
    return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
}

async function atualizarSaldoExibido() {
    if (!clienteLogado) return;
    try {
        const res = await fetch(`${API}/clientes/${clienteLogado.id}/extrato`);
        const data = await res.json();
        const saldo = data.saldo || 0;
        clienteLogado.saldo = saldo;
        document.getElementById('saldo-valor').innerText = formatarMoeda(saldo);
        if (document.getElementById('extrato-saldo')) {
            document.getElementById('extrato-saldo').innerText = formatarMoeda(saldo);
        }
    } catch (err) {
        console.error('Erro ao atualizar saldo', err);
    }
}

async function carregarClientes() {
    try {
        const res = await fetch(`${API}/clientes`);
        const clientes = await res.json();
        const sel = document.getElementById('select-cliente');
        if (!clientes.length) {
            sel.innerHTML = '<option value="">Nenhum cliente cadastrado</option>';
            return;
        }
        sel.innerHTML = '<option value="">Selecione...</option>' +
            clientes.map(c => `<option value="${c.id}" data-saldo="${c.saldo}">${c.nome}</option>`).join('');
    } catch (err) {
        document.getElementById('select-cliente').innerHTML = '<option value="">Erro ao carregar</option>';
        toast('Erro ao conectar com o servidor', 'error');
    }
}

function switchTab(tab) {
    const isLogin = tab === 'login';
    document.getElementById('form-login').style.display = isLogin ? 'block' : 'none';
    document.getElementById('form-cadastro').style.display = isLogin ? 'none' : 'block';
    const btnLogin = document.getElementById('tab-login');
    const btnCad = document.getElementById('tab-cadastro');
    if (isLogin) {
        btnLogin.classList.add('active');
        btnCad.classList.remove('active');
    } else {
        btnCad.classList.add('active');
        btnLogin.classList.remove('active');
    }
}

async function cadastrarCliente() {
    const nome = document.getElementById('input-nome').value.trim();
    if (!nome) { toast('Digite seu nome!', 'error'); return; }
    try {
        const res = await fetch(`${API}/clientes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, saldo: 0 })
        });
        if (!res.ok) throw new Error();
        toast('Conta criada com sucesso!', 'success');
        switchTab('login');
        await carregarClientes();
    } catch {
        toast('Erro ao cadastrar. Verifique se o back-end está rodando.', 'error');
    }
}

function fazerLogin() {
    const sel = document.getElementById('select-cliente');
    const opt = sel.options[sel.selectedIndex];
    if (!opt || !opt.value) { toast('Selecione um cliente!', 'error'); return; }
    clienteLogado = {
        id: parseInt(opt.value),
        nome: opt.text,
        saldo: parseFloat(opt.dataset.saldo) || 0
    };
    document.getElementById('page-login').classList.remove('active');
    document.getElementById('navbar').style.display = 'flex';
    atualizarSaldoExibido();  // busca saldo atualizado do back
    renderCardapio();
    renderNotas();
    carregarSlots();          // pré-carrega slots para a página
    showPage('home');
    toast(`Bem-vindo(a), ${clienteLogado.nome}! 🍗`, 'success');
}

function logout() {
    clienteLogado = null;
    notaSelecionada = null;
    document.getElementById('navbar').style.display = 'none';
    document.getElementById('page-login').classList.add('active');
    document.getElementById('page-home').classList.remove('active');
    document.getElementById('page-extrato').classList.remove('active');
    document.getElementById('page-slots').classList.remove('active');
    carregarClientes();
}

function showPage(pageName) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(`page-${pageName}`).classList.add('active');
    if (pageName === 'extrato') carregarExtrato();
    if (pageName === 'slots') carregarSlots();
    document.querySelectorAll('.nav-links a').forEach(a => a.classList.remove('active'));
    const activeLink = document.getElementById(`nav-${pageName}`);
    if (activeLink) activeLink.classList.add('active');
}

function renderNotas() {
    const grid = document.getElementById('notas-grid');
    if (!grid) return;
    grid.innerHTML = NOTAS.map(n => `
        <button class="nota-btn" onclick="selecionarNota(${n}, this)">R$ ${n}</button>
    `).join('');
}

function selecionarNota(valor, btn) {
    notaSelecionada = valor;
    document.querySelectorAll('.nota-btn').forEach(b => b.classList.remove('selected'));
    btn.classList.add('selected');
    document.getElementById('nota-selecionada').innerText = `R$ ${valor}`;
}

async function inserirCredito() {
    if (!clienteLogado) { toast('Faça login primeiro', 'error'); return; }
    if (!notaSelecionada) { toast('Selecione uma nota!', 'error'); return; }
    try {
        const res = await fetch(`${API}/credito`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                clienteId: clienteLogado.id,
                valorNota: notaSelecionada
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.mensagem || 'Erro ao inserir crédito');
        toast(data.mensagem, 'success');
        notaSelecionada = null;
        document.querySelectorAll('.nota-btn').forEach(b => b.classList.remove('selected'));
        document.getElementById('nota-selecionada').innerText = '—';
        await atualizarSaldoExibido();
    } catch (err) {
        toast(err.message, 'error');
    }
}

async function comprarCoxinha(sabor, preco) {
    if (!clienteLogado) { toast('Faça login', 'error'); return; }
    if (clienteLogado.saldo < preco) {
        toast(`Saldo insuficiente! Seu saldo: R$ ${formatarMoeda(clienteLogado.saldo)}`, 'error');
        return;
    }
    try {
        const res = await fetch(`${API}/compras`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                clienteId: clienteLogado.id,
                sabor: sabor
                // não envia notaInserida, pois o back usa o saldo acumulado
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.mensagem || 'Erro na compra');
        toast(`${data.mensagem} | Troco: R$ ${formatarMoeda(data.troco)}`, 'success');
        await atualizarSaldoExibido();
        await carregarExtrato();
    } catch (err) {
        toast(err.message, 'error');
    }
}

function renderCardapio() {
    const grid = document.getElementById('cardapio-grid');
    if (!grid) return;
    grid.innerHTML = CARDAPIO.map((item, i) => `
        <div class="coxinha-card fade-in" style="animation-delay:${i*0.05}s" onclick="comprarCoxinha('${item.sabor}', ${item.preco})">
            <div class="coxinha-emoji">${item.emoji}</div>
            <h3>${item.sabor}</h3>
            <div class="coxinha-price">R$ ${formatarMoeda(item.preco)}</div>
        </div>
    `).join('');
}

function popularSelectsTroca() {
    const selectOrigem = document.getElementById('sabor-origem');
    const selectDestino = document.getElementById('sabor-destino');
    if (!selectOrigem) return;
    const opcoes = CARDAPIO.map(item => `<option value="${item.sabor}">${item.sabor}</option>`).join('');
    selectOrigem.innerHTML = '<option value="">Sabor original</option>' + opcoes;
    selectDestino.innerHTML = '<option value="">Novo sabor</option>' + opcoes;
}

async function trocarSabor() {
    const origem = document.getElementById('sabor-origem').value;
    const destino = document.getElementById('sabor-destino').value;
    if (!origem || !destino) { toast('Selecione ambos os sabores', 'error'); return; }
    if (!clienteLogado) { toast('Faça login', 'error'); return; }
    try {
        const res = await fetch(`${API}/trocar-sabor`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                clienteId: clienteLogado.id,
                saborOrigem: origem,
                saborDestino: destino
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.mensagem || 'Erro na troca');
        toast(data.mensagem, 'success');
        await atualizarSaldoExibido();
        await carregarExtrato();
    } catch (err) {
        toast(err.message, 'error');
    }
}

async function carregarExtrato() {
    if (!clienteLogado) return;
    try {
        const res = await fetch(`${API}/clientes/${clienteLogado.id}/extrato`);
        const data = await res.json();
        document.getElementById('extrato-saldo').innerText = formatarMoeda(data.saldo || 0);
        document.getElementById('extrato-sub').innerHTML = `Olá, ${data.cliente}! Histórico completo.`;
        const tbody = document.getElementById('extrato-tbody');
        const movs = data.movimentacoes || [];
        if (movs.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4"><div class="empty-state">🍽️ Nenhuma movimentação ainda</div></td></tr>`;
            return;
        }
        tbody.innerHTML = movs.slice().reverse().map(m => `
            <tr>
                <td>${formatarData(m.dataHora)}</td>
                <td>${m.sabor || '—'}</td>
                <td><span class="badge ${m.tipoOperacao === 'COMPRA' ? 'badge-compra' : 'badge-troca'}">${m.tipoOperacao}</span></td>
                <td>R$ ${formatarMoeda(m.valor)}</td>
            </tr>
        `).join('');
    } catch (err) {
        toast('Erro ao carregar extrato', 'error');
    }
}

async function carregarSlots() {
    try {
        const res = await fetch(`${API}/slots`);
        const slots = await res.json();
        const grid = document.getElementById('slots-grid');
        if (!slots.length) {
            grid.innerHTML = '<div>Nenhum slot configurado</div>';
            return;
        }
        const ordenados = slots.sort((a,b) => a.valorNota - b.valorNota);
        grid.innerHTML = ordenados.map(s => `
            <div class="slot-card">
                <div class="slot-valor">R$ ${s.valorNota}</div>
                <div class="slot-qtd">${s.qtd} unidades</div>
            </div>
        `).join('');
    } catch (err) {
        document.getElementById('slots-grid').innerHTML = '<div>Erro ao carregar slots</div>';
    }
}

window.addEventListener('DOMContentLoaded', () => {
    carregarClientes();
    popularSelectsTroca();
    setInterval(() => {
        if (clienteLogado) atualizarSaldoExibido();
    }, 10000);
});