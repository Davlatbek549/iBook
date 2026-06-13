/* DZ Ink & Paper — shared screen primitives + demo data.
   Exposes window.DZInk = { Ic, pal, cardStyle, Frame, Nav, Chip, Label, IconBtn, TopBar,
   Btn, Field, SectionTitle, BookRow, Stars, ToggleSwitch, Dots, COVER, AV, BOOKS, FRIENDS, CATS } */
(function () {
  const COVER = '../assets/covers/';
  const AV = '../assets/avatars/';

  function Ic({ n, s = 20, color = 'currentColor', style }) {
    const def = (window.DZICO || {})[n];
    if (!def) return <span style={{ display: 'inline-block', width: s, height: s, flexShrink: 0, ...style }} />;
    return <span style={{ display: 'inline-flex', width: s, height: s, color, flexShrink: 0, ...style }} dangerouslySetInnerHTML={{ __html: `<svg width="${s}" height="${s}" viewBox="${def.vb}" fill="none" style="display:block">${def.p}</svg>` }} />;
  }

  const pal = (t, mode) => (mode === 'D' ? t.D : t.L);
  function cardStyle(t, c) {
    if (t.card === 'soft') return { background: c.surface, borderRadius: t.radius, boxShadow: '0 6px 22px rgba(20,18,12,0.07)' };
    if (t.card === 'rich') return { background: c.surface, borderRadius: t.radius, border: `1px solid ${c.line}`, boxShadow: '0 4px 16px rgba(20,18,12,0.05)' };
    return { background: c.surface, borderRadius: t.radius, border: `1px solid ${c.line}` };
  }
  const Label = ({ t, c, children, style }) => (
    <div style={{ font: `700 10.5px/1 ${t.fontBody}`, letterSpacing: '0.14em', textTransform: 'uppercase', color: c.muted, ...style }}>{children}</div>
  );

  function Frame({ t, c, h, children }) {
    return (
      <div style={{ width: '100%', height: h, background: c.paper, color: c.ink, fontFamily: t.fontBody, position: 'relative', overflow: 'hidden', display: 'flex', flexDirection: 'column', borderRadius: 26 }}>
        <div style={{ height: 40, display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 22px', flexShrink: 0 }}>
          <span style={{ font: `700 13px/1 ${t.fontBody}`, color: c.ink }}>9:41</span>
          <span style={{ display: 'flex', gap: 5, alignItems: 'center', opacity: 0.85 }}>
            <svg width="16" height="10" viewBox="0 0 16 10" fill={c.ink}><rect x="0" y="6" width="2.6" height="4" rx="1"/><rect x="4" y="4" width="2.6" height="6" rx="1"/><rect x="8" y="2" width="2.6" height="8" rx="1"/><rect x="12" y="0" width="2.6" height="10" rx="1"/></svg>
            <svg width="22" height="10" viewBox="0 0 22 10" fill="none"><rect x="0.5" y="0.5" width="18" height="9" rx="2.4" stroke={c.ink} opacity="0.4"/><rect x="2" y="2" width="14" height="6" rx="1.2" fill={c.ink}/><rect x="20" y="3" width="1.5" height="4" rx="0.7" fill={c.ink} opacity="0.5"/></svg>
          </span>
        </div>
        <div style={{ flex: 1, overflow: 'hidden' }}>{children}</div>
      </div>
    );
  }

  function Chip({ t, c, children, solid }) {
    return (
      <span style={{ display: 'inline-flex', alignItems: 'center', height: 26, padding: '0 12px', borderRadius: 999, font: `600 11px/1 ${t.fontBody}`,
        background: solid ? c.accentSoft : 'transparent', color: solid ? c.accent : c.inkSoft, border: solid ? 'none' : `1px solid ${c.line}` }}>{children}</span>
    );
  }

  function Nav({ t, c, active = 'home' }) {
    const items = [['home', 'home'], ['search', 'search'], ['book', 'library'], ['shop', 'store'], ['user', 'you']];
    return (
      <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, height: 64, background: c.paper, borderTop: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'space-around', paddingBottom: 6 }}>
        {items.map(([icon, key]) => {
          const on = key === active;
          return (
            <div key={key} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 5 }}>
              <Ic n={icon} s={21} color={on ? c.accent : c.muted} />
              <span style={{ width: 4, height: 4, borderRadius: 999, background: on ? c.accent : 'transparent' }} />
            </div>
          );
        })}
      </div>
    );
  }

  const IconBtn = ({ t, c, n, s = 18, solid }) => (
    <div style={{ width: 40, height: 40, borderRadius: t.radiusSm, flexShrink: 0,
      border: solid ? 'none' : `1px solid ${c.line}`, background: solid ? c.accent : 'transparent',
      display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <Ic n={n} s={s} color={solid ? c.onAccent : c.ink} />
    </div>
  );

  function TopBar({ t, c, title, sub, right, noBack }) {
    return (
      <div style={{ display: 'flex', alignItems: 'center', gap: 14, padding: '4px 22px 14px' }}>
        {!noBack && <IconBtn t={t} c={c} n="back" />}
        <div style={{ flex: 1, minWidth: 0 }}>
          {title && <div style={{ font: `${t.displayWeight} 19px/1.15 ${t.fontDisplay}`, color: c.ink, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{title}</div>}
          {sub && <div style={{ font: `400 11.5px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 3 }}>{sub}</div>}
        </div>
        {right}
      </div>
    );
  }

  function Btn({ t, c, children, kind = 'primary', h = 52, style }) {
    const base = { height: h, padding: '0 20px', borderRadius: t.radiusSm + 2, font: `700 14.5px/1 ${t.fontBody}`, cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 9, border: 'none' };
    const kinds = {
      primary: { background: c.accent, color: c.onAccent },
      secondary: { background: 'transparent', color: c.ink, border: `1px solid ${c.line}`, fontWeight: 600 },
      soft: { background: c.accentSoft, color: c.accent },
      ghost: { background: 'transparent', color: c.accent },
    };
    return <button style={{ ...base, ...kinds[kind], ...style }}>{children}</button>;
  }

  function Field({ t, c, icon, value, placeholder, trailing, focus }) {
    return (
      <div style={{ height: 50, borderRadius: t.radiusSm + 2, border: `1px solid ${focus ? c.accent : c.line}`, background: c.surface, display: 'flex', alignItems: 'center', gap: 11, padding: '0 15px' }}>
        {icon && <Ic n={icon} s={17} color={focus ? c.accent : c.muted} />}
        <span style={{ flex: 1, font: `${value ? 500 : 400} 13.5px/1 ${t.fontBody}`, color: value ? c.ink : c.muted, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{value || placeholder}</span>
        {trailing}
      </div>
    );
  }

  const SectionTitle = ({ t, c, children, action, style }) => (
    <div style={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', ...style }}>
      <div style={{ font: `${t.displayWeight} 18px/1 ${t.fontDisplay}`, color: c.ink }}>{children}</div>
      {action && <span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>{action}</span>}
    </div>
  );

  const Stars = ({ c, n = 5, of = 5, s = 12 }) => (
    <span style={{ display: 'inline-flex', gap: 2 }}>
      {Array.from({ length: of }, (_, i) => <Ic key={i} n="star" s={s} color={i < n ? c.accent : c.line} />)}
    </span>
  );

  function BookRow({ t, c, b, meta, trailing, top }) {
    return (
      <div style={{ display: 'flex', gap: 14, alignItems: 'center', padding: '13px 0', borderTop: top ? `1px solid ${c.line}` : 'none' }}>
        <img src={b.c} alt="" style={{ width: 46, height: 66, borderRadius: t.cover, objectFit: 'cover', flexShrink: 0, boxShadow: '0 5px 12px rgba(20,18,12,0.14)' }} />
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ font: `${t.displayWeight} 15px/1.2 ${t.fontDisplay}`, color: c.ink, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{b.t}</div>
          <div style={{ font: `400 12px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 3 }}>{b.a}</div>
          {meta && <div style={{ marginTop: 6 }}>{meta}</div>}
        </div>
        {trailing}
      </div>
    );
  }

  const ToggleSwitch = ({ c, on }) => (
    <span style={{ width: 40, height: 24, borderRadius: 999, background: on ? c.accent : c.alt, border: `1px solid ${on ? c.accent : c.line}`, display: 'inline-flex', alignItems: 'center', padding: 2, justifyContent: on ? 'flex-end' : 'flex-start', flexShrink: 0 }}>
      <span style={{ width: 18, height: 18, borderRadius: 999, background: on ? c.onAccent : c.surface, boxShadow: '0 1px 3px rgba(20,18,12,0.2)' }} />
    </span>
  );

  const Dots = ({ c, n = 3, at = 0 }) => (
    <span style={{ display: 'inline-flex', gap: 6, alignItems: 'center' }}>
      {Array.from({ length: n }, (_, i) => <span key={i} style={{ width: i === at ? 18 : 5, height: 5, borderRadius: 999, background: i === at ? c.accent : c.line, transition: 'width .2s' }} />)}
    </span>
  );

  const BOOKS = [
    { t: 'Mexican Gothic', a: 'Silvia Moreno-Garcia', c: COVER + 'book_cover.png', g: ['Literary', 'Gothic'], price: '12.99', rating: '4.6', pages: 320 },
    { t: 'The Archer', a: 'Paulo Coelho', c: COVER + 'book_cover_2.png', g: ['Fable', 'Wisdom'], price: '8.50', rating: '4.4', pages: 160 },
    { t: 'Red at the Bone', a: 'Jacqueline Woodson', c: COVER + 'book_cover_3.png', g: ['Literary', 'Family'], price: '10.00', rating: '4.5', pages: 208 },
    { t: 'Bestiary', a: 'K-Ming Chang', c: COVER + 'book_cover_4.png', g: ['Myth', 'Debut'], price: '9.20', rating: '4.2', pages: 272 },
    { t: 'Olive, Again', a: 'Elizabeth Strout', c: COVER + 'olive_again_book.png', g: ['Literary', 'Quiet'], price: '13.00', rating: '4.3', pages: 304 },
    { t: 'Bestiary', a: 'K-Ming Chang', c: COVER + 'author_detail_bestiary.png', g: ['Myth'], price: '9.20', rating: '4.2', pages: 272 },
    { t: 'Red at the Bone', a: 'Jacqueline Woodson', c: COVER + 'author_detail_red_at_bone.png', g: ['Family'], price: '10.00', rating: '4.5', pages: 208 },
    { t: 'Olive, Again', a: 'Elizabeth Strout', c: COVER + 'book_olive_again.png', g: ['Quiet'], price: '13.00', rating: '4.3', pages: 304 },
  ];

  const FRIENDS = [
    { n: 'Patricia Lane', h: '@patricia.reads', av: AV + 'profile_2.png', on: true },
    { n: 'Daniel Moreau', h: '@dmoreau', av: AV + 'profile_3.png', on: false },
    { n: 'Maria Renzy', h: '@maria.renzy', av: AV + 'img_maria_renzy.png', on: true },
    { n: 'Neil Alvin', h: '@neilalvin', av: AV + 'img_neil_alvin.png', on: false },
    { n: 'Yza Barretto', h: '@yza.b', av: AV + 'img_yza_barretto.png', on: true },
    { n: 'Raunak Purohit', h: '@raunakp', av: AV + 'img_raunak_purohit.png', on: false },
  ];

  const CATS = ['Literary fiction', 'History', 'Romance', 'Essays', 'Poetry', 'Biography', 'Fantasy', 'Health'];

  window.DZInk = { Ic, pal, cardStyle, Frame, Nav, Chip, Label, IconBtn, TopBar, Btn, Field, SectionTitle, BookRow, Stars, ToggleSwitch, Dots, COVER, AV, BOOKS, FRIENDS, CATS };
})();
