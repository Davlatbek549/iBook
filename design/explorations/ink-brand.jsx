/* DZ Ink & Paper — brand artboards: app icon + bespoke icon set grid.
   Exposes window.DZInkBrand = { AppIcon, IconSet } */
(function () {
  const { Ic, pal, Frame, Label } = window.DZInk;

  /* ---------- App icon ---------- */
  function Tile({ t, size, bg, fg, border, mark }) {
    const r = size * 0.225;
    return (
      <div style={{ width: size, height: size, borderRadius: r, background: bg, border: border ? `1px solid ${border}` : 'none',
        display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
        boxShadow: '0 10px 26px rgba(20,18,12,0.18)' }}>
        {mark && <Ic n="book_open" s={size * 0.21} color={mark} style={{ marginBottom: size * 0.05 }} />}
        <div style={{ font: `${t.displayWeight} ${size * 0.34}px/1 ${t.fontDisplay}`, color: fg, letterSpacing: '-0.01em' }}>DZ</div>
      </div>
    );
  }

  function AppIcon({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const inkBg = mode === 'D' ? '#211C16' : c.ink; /* keep the launcher tile inky in both modes */
    const paperFg = mode === 'D' ? '#EFE7D8' : c.paper;
    return (
      <Frame t={t} c={c} h={480}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '0 26px', gap: 0 }}>
          <Tile t={t} size={132} bg={inkBg} fg={paperFg} mark={c.accent} />
          <div style={{ font: `400 12.5px/1.5 ${t.fontAccent}`, color: c.muted, fontStyle: 'italic', marginTop: 18 }}>Primary — ink ground</div>
          <div style={{ display: 'flex', alignItems: 'flex-end', gap: 18, marginTop: 30 }}>
            <Tile t={t} size={72} bg={c.accent} fg={c.onAccent} />
            <Tile t={t} size={72} bg={c.surface} fg={c.ink} border={c.line} mark={c.accent} />
            <Tile t={t} size={44} bg={inkBg} fg={paperFg} />
            <Tile t={t} size={28} bg={inkBg} fg={paperFg} />
          </div>
          <div style={{ font: `400 12.5px/1.5 ${t.fontAccent}`, color: c.muted, fontStyle: 'italic', marginTop: 18 }}>Accent · paper · scale checks</div>
        </div>
      </Frame>
    );
  }

  /* ---------- Icon set grid ---------- */
  const ICON_ORDER = [
    ['Navigate', ['home', 'search', 'book', 'shop', 'user', 'back', 'close', 'plus', 'done', 'move']],
    ['Act', ['share', 'send', 'bookmark', 'favorite', 'star', 'delete', 'camera', 'book_open']],
    ['Talk', ['chat', 'message', 'email', 'bell', 'notification', 'at', 'flag']],
    ['Account', ['lock', 'password', 'profile', 'settings', 'appearance']],
    ['Commerce', ['tag', 'premium', 'gift', 'purchased', 'stats', 'calendar', 'terms', 'policy']],
  ];

  function IconSet({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={960}>
        <div style={{ padding: '26px 24px 24px' }}>
          <div style={{ font: `${t.displayWeight} 22px/1.15 ${t.fontDisplay}`, color: c.ink }}>Bespoke icon set</div>
          <div style={{ font: `400 12px/1.6 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>24px grid · 1.7px ink stroke · round caps · drawn, not assembled</div>
          {ICON_ORDER.map(([group, names]) => (
            <div key={group} style={{ marginTop: 20 }}>
              <Label t={t} c={c}>{group}</Label>
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(5, 1fr)', gap: '14px 6px', marginTop: 12 }}>
                {names.map((n) => (
                  <div key={n} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 7 }}>
                    <div style={{ width: 44, height: 44, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, background: c.surface, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                      <Ic n={n} s={22} color={c.ink} />
                    </div>
                    <span style={{ font: `500 8.5px/1 ${t.fontBody}`, letterSpacing: '0.04em', color: c.muted }}>{n.replace('_', ' ')}</span>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </Frame>
    );
  }

  window.DZInkBrand = { AppIcon, IconSet };
})();
