/* DZ Ink & Paper — membership & settings screens.
   Exposes window.DZInkMembership = { Membership, PremiumMembership, Settings } */
(function () {
  const { Ic, pal, cardStyle, Frame, Label, IconBtn, TopBar, Btn, ToggleSwitch, Chip } = window.DZInk;

  const Benefit = ({ t, c, children, dim }) => (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
      <div style={{ width: 24, height: 24, borderRadius: 999, background: dim ? c.alt : c.accentSoft, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
        <Ic n="done" s={9} color={dim ? c.muted : c.accent} />
      </div>
      <span style={{ font: `400 13.5px/1.45 ${t.fontBody}`, color: c.inkSoft }}>{children}</span>
    </div>
  );

  /* ---------- Membership (upsell, free plan) ---------- */
  function Membership({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={860}>
        <div style={{ height: '100%', position: 'relative', paddingBottom: 96 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 22px 0' }}>
            <IconBtn t={t} c={c} n="back" />
          </div>

          <div style={{ padding: '16px 26px 0' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 7, height: 28, padding: '0 13px', borderRadius: 999, background: c.accentSoft, font: `700 11px/1 ${t.fontBody}`, letterSpacing: '0.1em', textTransform: 'uppercase', color: c.accent }}>
              <Ic n="premium" s={13} />DZ Premium
            </span>
            <div style={{ font: `${t.displayWeight} 32px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 16 }}>The whole library,<br />always open</div>
            <p style={{ font: `400 13.5px/1.65 ${t.fontBody}`, color: c.muted, margin: '12px 0 0' }}>One plan for unlimited reading — built for people who finish books.</p>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: 13, padding: '22px 26px 0' }}>
            <Benefit t={t} c={c}>Unlimited access to 12,000+ titles</Benefit>
            <Benefit t={t} c={c}>Offline reading on three devices</Benefit>
            <Benefit t={t} c={c}>2× coins on every purchase</Benefit>
            <Benefit t={t} c={c}>Early access to new releases</Benefit>
          </div>

          {/* plans */}
          <div style={{ display: 'flex', gap: 12, padding: '24px 22px 0' }}>
            <div style={{ flex: 1, ...cardStyle(t, c), padding: '16px 16px 18px' }}>
              <Label t={t} c={c}>Monthly</Label>
              <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink, marginTop: 12 }}>$7.99</div>
              <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>per month</div>
            </div>
            <div style={{ flex: 1, ...cardStyle(t, c), padding: '16px 16px 18px', border: `1.5px solid ${c.accent}`, position: 'relative' }}>
              <span style={{ position: 'absolute', top: -11, right: 12, height: 22, padding: '0 10px', borderRadius: 999, background: c.accent, color: c.onAccent, font: `700 10px/22px ${t.fontBody}`, letterSpacing: '0.06em' }}>SAVE 37%</span>
              <Label t={t} c={c} style={{ color: c.accent }}>Yearly</Label>
              <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink, marginTop: 12 }}>$59.99</div>
              <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>$5.00 / month</div>
            </div>
          </div>

          <p style={{ font: `400 11.5px/1.6 ${t.fontBody}`, color: c.muted, margin: '16px 26px 0', textAlign: 'center' }}>
            7-day free trial · cancel anytime · keep your purchased books forever
          </p>

          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, padding: '14px 22px 18px', background: c.paper, borderTop: `1px solid ${c.line}` }}>
            <Btn t={t} c={c}>Start free trial</Btn>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Premium membership (active) ---------- */
  function PremiumMembership({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Membership" />

          {/* status card — the one solid-accent moment */}
          <div style={{ margin: '8px 22px 0', borderRadius: t.radius + 4, background: c.accent, color: c.onAccent, padding: 20, boxShadow: '0 16px 34px rgba(20,18,12,0.28)' }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 8, font: `700 11px/1 ${t.fontBody}`, letterSpacing: '0.12em', textTransform: 'uppercase', opacity: 0.85 }}>
                <Ic n="premium" s={14} />Premium · yearly
              </span>
              <Ic n="book_open" s={18} style={{ opacity: 0.6 }} />
            </div>
            <div style={{ font: `${t.displayWeight} 26px/1.15 ${t.fontDisplay}`, marginTop: 16 }}>Amelia Hartwell</div>
            <div style={{ font: `400 12.5px/1.4 ${t.fontBody}`, opacity: 0.85, marginTop: 6 }}>Member since March 2025</div>
            <div style={{ height: 1, background: 'currentColor', opacity: 0.25, margin: '16px 0 12px' }} />
            <div style={{ display: 'flex', justifyContent: 'space-between', font: `400 12px/1 ${t.fontBody}` }}>
              <span style={{ opacity: 0.85 }}>Renews May 12, 2027</span>
              <span style={{ fontWeight: 700 }}>$59.99 / yr</span>
            </div>
          </div>

          {/* usage */}
          <div style={{ display: 'flex', gap: 12, margin: '16px 22px 0' }}>
            {[['Read this year', '18 books'], ['Saved vs. buying', '$112']].map((s, i) => (
              <div key={i} style={{ flex: 1, ...cardStyle(t, c), padding: '14px 16px' }}>
                <Label t={t} c={c}>{s[0]}</Label>
                <div style={{ font: `${t.displayWeight} 20px/1 ${t.fontDisplay}`, color: c.ink, marginTop: 10 }}>{s[1]}</div>
              </div>
            ))}
          </div>

          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>Your benefits</Label>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 14 }}>
              <Benefit t={t} c={c}>Unlimited library access</Benefit>
              <Benefit t={t} c={c}>Offline on 3 devices · 2 in use</Benefit>
              <Benefit t={t} c={c}>2× coins · 240 earned so far</Benefit>
            </div>
          </div>

          <div style={{ margin: '24px 22px 0', ...cardStyle(t, c) }}>
            {[['calendar', 'Change billing date'], ['purchased', 'Billing history'], ['close', 'Cancel membership']].map((r, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '14px 16px', borderTop: i ? `1px solid ${c.line}` : 'none' }}>
                <Ic n={r[0]} s={16} color={i === 2 ? (mode === 'D' ? '#D96A55' : '#B3402A') : c.inkSoft} />
                <span style={{ flex: 1, font: `600 13px/1 ${t.fontBody}`, color: i === 2 ? (mode === 'D' ? '#D96A55' : '#B3402A') : c.ink }}>{r[1]}</span>
                {i < 2 && <span style={{ transform: 'rotate(180deg)', display: 'flex' }}><Ic n="back" s={13} color={c.muted} /></span>}
              </div>
            ))}
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Settings ---------- */
  function Settings({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const Group = ({ label, rows }) => (
      <div style={{ padding: '20px 22px 0' }}>
        <Label t={t} c={c}>{label}</Label>
        <div style={{ ...cardStyle(t, c), marginTop: 11 }}>
          {rows.map((r, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '13px 16px', borderTop: i ? `1px solid ${c.line}` : 'none' }}>
              <Ic n={r[0]} s={16} color={c.inkSoft} />
              <span style={{ flex: 1, font: `600 13px/1 ${t.fontBody}`, color: c.ink }}>{r[1]}</span>
              {r[2]}
            </div>
          ))}
        </div>
      </div>
    );
    const chev = <span style={{ transform: 'rotate(180deg)', display: 'flex' }}><Ic n="back" s={13} color={c.muted} /></span>;
    const val = (s) => <span style={{ font: `400 12px/1 ${t.fontBody}`, color: c.muted, display: 'flex', alignItems: 'center', gap: 8 }}>{s}{chev}</span>;
    return (
      <Frame t={t} c={c} h={880}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Settings" />

          <Group label="Account" rows={[
            ['user', 'Edit profile', chev],
            ['email', 'Email', val('amelia@hartwell.co')],
            ['password', 'Password', chev],
          ]} />

          <Group label="Reading" rows={[
            ['appearance', 'Appearance', val(mode === 'D' ? 'Dark' : 'Light')],
            ['text_size', 'Text size', val('Medium')],
            ['stats', 'Daily goal', val('30 min')],
          ]} />

          <Group label="Notifications" rows={[
            ['bell', 'Reading reminders', <ToggleSwitch c={c} on />],
            ['chat', 'Messages', <ToggleSwitch c={c} on />],
            ['tag', 'Price drops', <ToggleSwitch c={c} />],
          ]} />

          <Group label="About" rows={[
            ['help_centre', 'Help centre', chev],
            ['terms', 'Terms of service', chev],
            ['policy', 'Privacy policy', chev],
          ]} />

          <div style={{ padding: '20px 22px 26px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 14 }}>
            <span style={{ font: `600 13px/1 ${t.fontBody}`, color: mode === 'D' ? '#D96A55' : '#B3402A' }}>Sign out</span>
            <span style={{ font: `400 10.5px/1 ${t.fontBody}`, color: c.muted }}>DZ for iOS · 3.2.1</span>
          </div>
        </div>
      </Frame>
    );
  }

  window.DZInkMembership = { Membership, PremiumMembership, Settings };
})();
